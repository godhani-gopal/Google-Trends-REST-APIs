package com.googleTrendsBigQuery.googleTrendsRestApis.service.serviceImpl;

import com.google.cloud.bigquery.FieldValueList;
import com.googleTrendsBigQuery.googleTrendsRestApis.entity.TopTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.exception.ResourceNotFoundException;
import com.googleTrendsBigQuery.googleTrendsRestApis.payload.TermAnalysis;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.BQRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.TopTermsRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.AIService;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.TopTermsService;
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
import java.util.Optional;

import static com.googleTrendsBigQuery.googleTrendsRestApis.util.BQFieldValueUtil.*;

@Service
public class TopTermsServiceImpl implements TopTermsService {

    private static final Logger logger = LoggerFactory.getLogger(TopTermsServiceImpl.class);
    private final TopTermsRepository topTermsRepository;
    private final QueryBuilder bqQueryBuilder;
    private final AIService aiService;
    private final BQRepository bqRepository;

    public TopTermsServiceImpl(TopTermsRepository topTermsRepository, QueryBuilder bqQueryBuilder, AIService aiService, BQRepository bqRepository) {
        this.topTermsRepository = topTermsRepository;
        this.bqQueryBuilder = bqQueryBuilder;
        this.aiService = aiService;
        this.bqRepository = bqRepository;
    }

    @Override
    public Page<TopTerms> getTopTerms(String term, String dmaName, String dmaId, String parsedWeek, Integer rank, Integer score, Pageable pageable) {
        try {
            LocalDate week = DateUtils.parseWeek(parsedWeek);


            Specification<TopTerms> spec = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                addPredicate(predicates, criteriaBuilder.like(root.get("term"), "%" + term + "%"), term);
                addPredicate(predicates, criteriaBuilder.like(root.get("dmaName"), "%" + dmaName + "%"), dmaName);
                addPredicate(predicates, criteriaBuilder.equal(root.get("dmaId"), dmaId), dmaId);
                addPredicate(predicates, criteriaBuilder.equal(root.get("week"), week), week);
                addPredicate(predicates, criteriaBuilder.equal(root.get("rank"), rank), rank);
                addPredicate(predicates, criteriaBuilder.equal(root.get("score"), score), score);

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };

            Page<TopTerms> result = topTermsRepository.findAll(spec, pageable);

            if (result.isEmpty()) {
                throw new ResourceNotFoundException("TopTerms", "No Data Found", dmaName);
            }

            return result;
        } catch (Exception e) {
            logger.error("Error while running getTopTerms ", e);
        }
        return null;
    }

    private void addPredicate(List<Predicate> predicates, Predicate predicate, Object value) {
        if (value != null && !(value instanceof String && ((String) value).isEmpty())) {
            predicates.add(predicate);
        }
    }
    @Override
    public TermAnalysis getPredictiveInsights(TopTerms topTerms) {
        // check if the topTerm exists in the database. use topTermsRepository to check.
        // return the value if exists, throw exception otherwise
        // do proper logging
        logger.info("Checking if the topTerm exists in the database: {}", topTerms);

        boolean existingTerm2 = topTermsRepository.existsByTermAndWeekAndScoreAndRankAndRefreshDateAndDmaNameAndDmaId(
                topTerms.getTerm(), topTerms.getWeek(), topTerms.getScore(),
                topTerms.getRank(), topTerms.getRefreshDate(),
                topTerms.getDmaName(), topTerms.getDmaId());

        // Check if the topTerm exists in the database based on its attributes



        if (existingTerm2) {
            logger.info("TopTerm exists in the database: {}", topTerms);
            // Return the value from AI service
            TermAnalysis result = aiService.getAIResults(topTerms);
            logger.info("Returning AI results for topTerm: {}", topTerms);
            return result;
        } else {
            logger.error("TopTerm does not exist in the database: {}", topTerms);
            // Throw custom exception
            throw new ResourceNotFoundException("TopTerms", "attributes", topTerms.toString());
        }

    }

    @Override
    public Long saveDataFromBQtoMySQL() {
        return bqRepository.saveDataFromBQtoMySQL(bqQueryBuilder::loadDataFromTopTermsQuery, this::mapToTopTerms,
                (batch, totalNumberOfRecordsSaved) -> {
                    topTermsRepository.saveAll(batch);
                    totalNumberOfRecordsSaved.addAndGet(batch.size());
                });
    }

    private TopTerms mapToTopTerms(FieldValueList values) {
        TopTerms topTerms = new TopTerms();
        topTerms.setRank(getIntegerValueOrNull(values, "rank"));
        topTerms.setRefreshDate(getLocalDateValueOrNull(values, "refresh_date"));
        topTerms.setDmaName(getStringValueOrNull(values, "dma_name"));
        topTerms.setDmaId(getStringValueOrNull(values, "dma_id"));
        topTerms.setTerm(getStringValueOrNull(values, "term"));
        topTerms.setWeek(getLocalDateValueOrNull(values, "week"));
        topTerms.setScore(getIntegerValueOrNull(values, "score"));
        return topTerms;
    }

    private LocalDate findLatestWeekValue() {
        return topTermsRepository.findLatestWeekValue()
                .orElseThrow(() -> new ResourceNotFoundException("week", "MAX(week)", "latest week cannot be found"));
    }

    @Override
    @Scheduled(cron = "0 0 0 4 * ?", zone = "UTC")
    public Long saveLatestDataFromBQtoMySQL() {
        try {
            LocalDate latestWeek = findLatestWeekValue();
            logger.info("Running scheduled data loading job for TopTerms for week after {}. Start time {}", latestWeek, LocalDateTime.now());
            Long totalRecords = bqRepository.saveDataFromBQtoMySQL(
                    () -> bqQueryBuilder.loadLatestDataFromTopTermsQuery(findLatestWeekValue()),
                    this::mapToTopTerms,
                    (batch, totalNumberOfRecordsSaved) -> {
                        topTermsRepository.saveAll(batch);
                        totalNumberOfRecordsSaved.addAndGet(batch.size());
                    });
            logger.info("Total {} records of TopTerms saved in database.", totalRecords);
            return totalRecords;
        } catch (Exception e) {
            logger.error("Error in saveLatestDataFromBQtoMySQL {}", logger.getClass().getName(), e);
        }
        return null;
    }
}
