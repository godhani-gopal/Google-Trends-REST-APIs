package com.googleTrendsBigQuery.googleTrendsRestApis.service.serviceImpl;

import com.google.cloud.bigquery.FieldValueList;
import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.exception.ResourceNotFoundException;
import com.googleTrendsBigQuery.googleTrendsRestApis.payload.TermAnalysis;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.BQRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.InternationalTopTermsRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.AIService;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.InternationalTopTermsService;
import com.googleTrendsBigQuery.googleTrendsRestApis.util.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.googleTrendsBigQuery.googleTrendsRestApis.util.BQFieldValueUtil.*;

@Service
public class InternationalTopTermsServiceImpl implements InternationalTopTermsService {

    private static final Logger logger = LoggerFactory.getLogger(InternationalTopTermsServiceImpl.class);
    QueryBuilder bqQueryBuilder;
    InternationalTopTermsRepository internationalTopTermsRepository;
    BQRepository bqRepository;
    AIService aiService;

    public InternationalTopTermsServiceImpl(QueryBuilder bqQueryBuilder, InternationalTopTermsRepository internationalTopTermsRepository, BQRepository bqRepository, AIService aiService) {
        this.bqQueryBuilder = bqQueryBuilder;
        this.internationalTopTermsRepository = internationalTopTermsRepository;
        this.bqRepository = bqRepository;
        this.aiService = aiService;
    }

    @Override
    public Long saveDataFromBQtoMySQL() {
        try {
            Long totalRecords = bqRepository.saveDataFromBQtoMySQL(
                    bqQueryBuilder::loadDataFromInternationalTopTermsQuery,
                    this::mapToInternationalTopTerms,
                    (batch, totalNumberOfRecordsSaved) -> {
                        internationalTopTermsRepository.saveAll(batch);
                        totalNumberOfRecordsSaved.addAndGet(batch.size());
                    });
            logger.info("Total {} records of InternationalTopRisingTerms saved in database.", totalRecords);
            return totalRecords;
        } catch (Exception e) {
            logger.error("Error in saveDataFromBQtoMySQL {}", logger.getClass().getName(), e);
        }
        return null;
    }

    @Override
    public LocalDate findLatestWeekValue() {
        return internationalTopTermsRepository.findLatestWeekValue()
                .orElseThrow(() -> {
                    logger.error("Error getting latest week from MySQL DB.");
                    return new ResourceNotFoundException("week", "MAX(week)", "latest week cannot be found");
                });
    }

    @Override
    public Page<InternationalTopTerms> getTerms(String term, LocalDate week, Integer score, Integer rank, LocalDate refreshDate, String countryName, String countryCode, String regionName, String regionCode, Integer numOfTerms, Pageable pageable) {
        return internationalTopTermsRepository.findTerms(term, week, score, rank, refreshDate, countryName, countryCode, regionName, regionCode, pageable);
    }

    @Override
    public List<String> getMatchingTerms(String term) {
        return internationalTopTermsRepository.searchTerms(term);
    }

    @Override
    @Scheduled(cron = "0 0 0 2 * ?", zone = "UTC")
    public Long saveLatestDataFromBQtoMySQL() {
        try {
            LocalDate latestWeek = findLatestWeekValue();
            logger.info("Running scheduled data loading job for InternationalTopRisingTerms for week after {}. Start time {}", latestWeek, LocalDateTime.now());

            Long totalRecords = bqRepository.saveDataFromBQtoMySQL(
                    () -> bqQueryBuilder.loadLatestDataFromInternationalTopTermsQuery(latestWeek),
                    this::mapToInternationalTopTerms,
                    (batch, totalNumberOfRecordsSaved) -> {
                        internationalTopTermsRepository.saveAll(batch);
                        totalNumberOfRecordsSaved.addAndGet(batch.size());
                    });
            logger.info("Total {} records of InternationalTopTerms added in database.", totalRecords);
            return totalRecords;
        } catch (Exception e) {
            logger.error("Error in saveLatestDataFromBQtoMySQL {}", logger.getClass().getName(), e);
        }
        return null;
    }

    private InternationalTopTerms mapToInternationalTopTerms(FieldValueList values) {
        InternationalTopTerms internationalTopTerms = new InternationalTopTerms();
        internationalTopTerms.setTerm(getStringValueOrNull(values, "term"));
        internationalTopTerms.setWeek(getLocalDateValueOrNull(values, "week"));
        internationalTopTerms.setScore(getIntegerValueOrNull(values, "score"));
        internationalTopTerms.setRank(getIntegerValueOrNull(values, "rank"));
        internationalTopTerms.setRefreshDate(getLocalDateValueOrNull(values, "refresh_date"));
        internationalTopTerms.setCountryName(getStringValueOrNull(values, "country_name"));
        internationalTopTerms.setCountryCode(getStringValueOrNull(values, "country_code"));
        internationalTopTerms.setRegionName(getStringValueOrNull(values, "region_name"));
        internationalTopTerms.setRegionCode(getStringValueOrNull(values, "region_code"));
        return internationalTopTerms;
    }

    @Override
    public TermAnalysis getPredictiveInsights(InternationalTopTerms internationalTopTerms) {
        return aiService.getAIResults(internationalTopTerms);
    }
}