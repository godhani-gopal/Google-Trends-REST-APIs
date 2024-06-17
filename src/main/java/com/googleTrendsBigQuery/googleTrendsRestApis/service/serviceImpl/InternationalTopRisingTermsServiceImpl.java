package com.googleTrendsBigQuery.googleTrendsRestApis.service.serviceImpl;

import com.google.cloud.bigquery.FieldValueList;
import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopRisingTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.exception.ResourceNotFoundException;
import com.googleTrendsBigQuery.googleTrendsRestApis.payload.TermAnalysis;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.BQRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.InternationalTopRisingTermsRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.AIService;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.InternationalTopRisingTermsService;
import com.googleTrendsBigQuery.googleTrendsRestApis.util.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.googleTrendsBigQuery.googleTrendsRestApis.util.BQFieldValueUtil.*;

@Service
public class InternationalTopRisingTermsServiceImpl implements InternationalTopRisingTermsService {

    BQRepository BQRepository;
    InternationalTopRisingTermsRepository internationalTopRisingTermsRepository;
    QueryBuilder bqQueryBuilder;
    AIService aiService;

    public InternationalTopRisingTermsServiceImpl(com.googleTrendsBigQuery.googleTrendsRestApis.repository.BQRepository BQRepository, InternationalTopRisingTermsRepository internationalTopRisingTermsRepository, QueryBuilder bqQueryBuilder, AIService aiService) {
        this.BQRepository = BQRepository;
        this.internationalTopRisingTermsRepository = internationalTopRisingTermsRepository;
        this.bqQueryBuilder = bqQueryBuilder;
        this.aiService = aiService;
    }

    @Override
    public Long saveDataFromBQtoMySQL() {
        BQRepository.saveDataFromBQtoMySQL(bqQueryBuilder::loadDataFromInternationalTopRisingTermsQuery,
                this::mapToInternationalTopRisingTerms,
                (batch, totalNumberOfRecordsSaved) -> {
                    internationalTopRisingTermsRepository.saveAll(batch);
                    totalNumberOfRecordsSaved.addAndGet(batch.size());
                });
        return null;
    }

    @Override
    public Long saveLatestDataFromBQtoMySQL() {
        LocalDate latestWeek = findLatestWeekValue();

        return BQRepository.saveDataFromBQtoMySQL(() -> bqQueryBuilder.loadLatestDataFromInternationalTopRisingTermsQuery(latestWeek),
                this::mapToInternationalTopRisingTerms,
                (batch, totalNumberOfRecordsSaved) -> {
                    internationalTopRisingTermsRepository.saveAll(batch);
                    totalNumberOfRecordsSaved.addAndGet(batch.size());
                });
    }

    @Override
    public PagedModel<InternationalTopRisingTerms> getTerms(String term, Integer percentGain, LocalDate week, Integer score, Integer rank, LocalDate refreshDate, String countryName, String countryCode, String regionName, String regionCode, Integer numOfTerms, Pageable pageable) {
        Page<InternationalTopRisingTerms> page = internationalTopRisingTermsRepository.findTerms(term, percentGain, week, score, rank, refreshDate, countryName, countryCode, regionName, regionCode, pageable);

        return PagedModel.of(page.getContent(), new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages()));
    }

    private InternationalTopRisingTerms mapToInternationalTopRisingTerms(FieldValueList values) {
        InternationalTopRisingTerms internationalTopRisingTerms = new InternationalTopRisingTerms();
        internationalTopRisingTerms.setTerm(getStringValueOrNull(values, "term"));
        internationalTopRisingTerms.setPercentGain(getIntegerValueOrNull(values, "percent_gain"));
        internationalTopRisingTerms.setWeek(getLocalDateValueOrNull(values, "week"));
        internationalTopRisingTerms.setScore(getIntegerValueOrNull(values, "score"));
        internationalTopRisingTerms.setRank(getIntegerValueOrNull(values, "rank"));
        internationalTopRisingTerms.setRefreshDate(getLocalDateValueOrNull(values, "refresh_date"));
        internationalTopRisingTerms.setCountryName(getStringValueOrNull(values, "country_name"));
        internationalTopRisingTerms.setCountryCode(getStringValueOrNull(values, "country_code"));
        internationalTopRisingTerms.setRegionName(getStringValueOrNull(values, "region_name"));
        internationalTopRisingTerms.setRegionCode(getStringValueOrNull(values, "region_code"));
        return internationalTopRisingTerms;
    }

    @Override
    public LocalDate findLatestWeekValue() {
        return internationalTopRisingTermsRepository.findLatestWeekValue()
                .orElseThrow(() -> new ResourceNotFoundException("week", "MAX(week)", "latest week cannot be found"));
    }

    @Override
    public TermAnalysis getPredictiveInsights(InternationalTopRisingTerms internationalTopRisingTerms) {
        return aiService.getAIResults(internationalTopRisingTerms);
    }
}
