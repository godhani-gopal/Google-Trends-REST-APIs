package com.googleTrendsBigQuery.googleTrendsRestApis.service.serviceImpl;

import com.google.cloud.bigquery.FieldValueList;
import com.googleTrendsBigQuery.googleTrendsRestApis.entity.TopRisingTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.exception.ResourceNotFoundException;
import com.googleTrendsBigQuery.googleTrendsRestApis.payload.TermAnalysis;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.BQRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.TopRisingTermsRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.AIService;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.TopRisingTermsService;
import com.googleTrendsBigQuery.googleTrendsRestApis.util.DateUtils;
import com.googleTrendsBigQuery.googleTrendsRestApis.util.QueryBuilder;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.googleTrendsBigQuery.googleTrendsRestApis.util.BQFieldValueUtil.*;

@Service
public class TopRisingTermsServiceImpl implements TopRisingTermsService {

    private static final Logger logger = LoggerFactory.getLogger(TopRisingTermsServiceImpl.class);
    private final TopRisingTermsRepository topRisingTermsRepository;
    private final BQRepository bqRepository;
    private final QueryBuilder bqQueryBuilder;
    private final AIService aiService;

    public TopRisingTermsServiceImpl(TopRisingTermsRepository topRisingTermsRepository, BQRepository bqRepository, QueryBuilder bqQueryBuilder, AIService aiService) {
        this.topRisingTermsRepository = topRisingTermsRepository;
        this.bqRepository = bqRepository;
        this.bqQueryBuilder = bqQueryBuilder;
        this.aiService = aiService;
    }

    @Override
    public Long saveDataFromBQtoMySQL() {
        return bqRepository.saveDataFromBQtoMySQL(
                bqQueryBuilder::loadDataFromTopRisingTermsQuery,
                this::mapToTopRisingTerms,
                (batch, totalNumberOfRecordsSaved) -> {
                    topRisingTermsRepository.saveAll(batch);
                    totalNumberOfRecordsSaved.addAndGet(batch.size());
                });
    }

    @Override
    @Scheduled(cron = "0 0 0 3 * ?", zone = "UTC")
    public Long saveLatestDataFromBQtoMySQL() {
        try {
            LocalDate latestWeek = findLatestWeekValue();
            logger.info("Running scheduled data loading job for TopRisingTerms for week after {}. Start time {}", latestWeek, LocalDateTime.now());
            Long totalRecords = bqRepository.saveDataFromBQtoMySQL(
                    () -> bqQueryBuilder.loadLatestDataFromTopRisingTermsQuery(findLatestWeekValue()),
                    this::mapToTopRisingTerms,
                    (batch, totalNumberOfSavedRecords) -> {
                        topRisingTermsRepository.saveAll(batch);
                        totalNumberOfSavedRecords.addAndGet(batch.size());
                    });
            logger.info("Total {} records of TopRisingTerms saved in database.", totalRecords);
            return totalRecords;
        } catch (Exception e) {
            logger.error("Error in saveLatestDataFromBQtoMySQL {}", logger.getClass().getName(), e);
        }
        return null;
    }

    @Override
    public LocalDate findLatestWeekValue() {
        return topRisingTermsRepository.findLatestWeekValue()
                .orElseThrow(() -> {
                    logger.error("Error getting latest week from MySQL DB.");
                    return new ResourceNotFoundException("week", "MAX(week)", "latest week cannot be found");
                });
    }

    @Override
    public Page<TopRisingTerms> getTopRisingTerms(String term, String dmaName, String dmaId, String parsedWeek, Integer rank, Integer score, Integer percentGain, Pageable pageable) {

        try {
            LocalDate week = DateUtils.parseWeek(parsedWeek);

            Specification<TopRisingTerms> spec = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                addPredicate(predicates, criteriaBuilder.like(root.get("term"), "%" + term + "%"), term);
                addPredicate(predicates, criteriaBuilder.like(root.get("dmaName"), "%" + dmaName + "%"), dmaName);
                addPredicate(predicates, criteriaBuilder.equal(root.get("dmaId"), dmaId), dmaId);
                addPredicate(predicates, criteriaBuilder.equal(root.get("week"), week), week);
                addPredicate(predicates, criteriaBuilder.equal(root.get("rank"), rank), rank);
                addPredicate(predicates, criteriaBuilder.equal(root.get("score"), score), score);
                addPredicate(predicates, criteriaBuilder.equal(root.get("percentGain"), percentGain), percentGain);
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };

            Page<TopRisingTerms> result = topRisingTermsRepository.findAll(spec, pageable);

            if (result == null || result.isEmpty()) {
                throw new ResourceNotFoundException("TopTerms", "No Data Found", dmaName);
            }

            return result;
        } catch (Exception e) {
            logger.error("Error in getTopRisingTerms ", e);
        }
        return null;
    }

    private void addPredicate(List<Predicate> predicates, Predicate predicate, Object value) {
        if (value != null && !(value instanceof String && ((String) value).isEmpty())) {
            predicates.add(predicate);
        }
    }

    private TopRisingTerms mapToTopRisingTerms(FieldValueList values) {
        TopRisingTerms topRisingTerms = new TopRisingTerms();
        topRisingTerms.setRank(getIntegerValueOrNull(values, "rank"));
        topRisingTerms.setPercentGain(getIntegerValueOrNull(values, "percent_gain"));
        topRisingTerms.setRefreshDate(getLocalDateValueOrNull(values, "refresh_date"));
        topRisingTerms.setDmaName(getStringValueOrNull(values, "dma_name"));
        topRisingTerms.setDmaId(getStringValueOrNull(values, "dma_id"));
        topRisingTerms.setTerm(getStringValueOrNull(values, "term"));
        topRisingTerms.setWeek(getLocalDateValueOrNull(values, "week"));
        topRisingTerms.setScore(getIntegerValueOrNull(values, "score"));
        return topRisingTerms;
    }

    @Override
    public TermAnalysis getPredictiveInsights(TopRisingTerms topRisingTerms) {
        return aiService.getAIResults(topRisingTerms);
    }
}
