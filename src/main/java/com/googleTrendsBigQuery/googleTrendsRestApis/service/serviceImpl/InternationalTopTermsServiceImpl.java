package com.googleTrendsBigQuery.googleTrendsRestApis.service.serviceImpl;

import com.google.cloud.bigquery.FieldValueList;
import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.BQRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.InternationalTopTermsRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.InternationalTopTermsService;
import com.googleTrendsBigQuery.googleTrendsRestApis.util.QueryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static com.googleTrendsBigQuery.googleTrendsRestApis.util.BQFieldValueUtil.*;

@Service
public class InternationalTopTermsServiceImpl implements InternationalTopTermsService {

    QueryBuilder bqQueryBuilder;
    InternationalTopTermsRepository internationalTopTermsRepository;
    BQRepository bqRepository;

    public InternationalTopTermsServiceImpl(@Qualifier("bqQueryBuilder") QueryBuilder bqQueryBuilder,
                                            InternationalTopTermsRepository internationalTopTermsRepository,
                                            BQRepository bqRepository) {
        this.bqQueryBuilder = bqQueryBuilder;
        this.internationalTopTermsRepository = internationalTopTermsRepository;
        this.bqRepository = bqRepository;
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
}