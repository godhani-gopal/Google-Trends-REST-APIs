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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.googleTrendsBigQuery.googleTrendsRestApis.util.BQFieldValueUtil.*;

@Service
public class InternationalTopTermsServiceImpl implements InternationalTopTermsService {

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
        return bqRepository.saveDataFromBQtoMySQL(
                bqQueryBuilder::loadDataFromInternationalTopTermsQuery,
                this::mapToInternationalTopTerms,
                (batch, totalNumberOfRecordsSaved) -> {
                    internationalTopTermsRepository.saveAll(batch);
                    totalNumberOfRecordsSaved.addAndGet(batch.size());
                });
    }

    @Override
    public LocalDate findLatestWeekValue() {
        return internationalTopTermsRepository.findLatestWeekValue()
                .orElseThrow(() -> new ResourceNotFoundException("week", "MAX(week)", "latest week cannot be found"));
    }

    @Override
    public Page<InternationalTopTerms> findTerms(String term, LocalDate week, Integer score, Integer rank, LocalDate refreshDate, String countryName, String countryCode, String regionName, String regionCode, Integer numOfTerms, Pageable pageable) {
        return internationalTopTermsRepository.findTerms(term, week, score, rank, refreshDate, countryName, countryCode, regionName, regionCode, pageable);
    }

    @Override
    public List<String> getMatchingTerms(String term) {
        return internationalTopTermsRepository.searchTerms(term);
    }

    @Override
    public Long saveLatestDataFromBQtoMySQL() {
        return bqRepository.saveDataFromBQtoMySQL(
                () -> bqQueryBuilder.loadLatestDataFromInternationalTopTermsQuery(findLatestWeekValue()),
                this::mapToInternationalTopTerms,
                (batch, totalNumberOfRecordsSaved) -> {
                    internationalTopTermsRepository.saveAll(batch);
                    totalNumberOfRecordsSaved.addAndGet(batch.size());
                });
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