package com.googleTrendsBigQuery.googleTrendsRestApis.service.serviceImpl;

import com.google.cloud.bigquery.FieldValueList;
import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopRisingTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.BQRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.InternationalTopRisingTermsRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.InternationalTopRisingTermsService;
import com.googleTrendsBigQuery.googleTrendsRestApis.util.QueryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static com.googleTrendsBigQuery.googleTrendsRestApis.util.BQFieldValueUtil.*;

@Service
public class InternationalTopRisingTermsServiceImpl implements InternationalTopRisingTermsService {

    BQRepository BQRepository;
    InternationalTopRisingTermsRepository internationalTopRisingTermsRepository;
    QueryBuilder bqQueryBuilder;

    public InternationalTopRisingTermsServiceImpl(BQRepository BQRepository,
                                                  InternationalTopRisingTermsRepository internationalTopRisingTermsRepository,
                                                  @Qualifier("bqQueryBuilder") QueryBuilder bqQueryBuilder) {
        this.BQRepository = BQRepository;
        this.internationalTopRisingTermsRepository = internationalTopRisingTermsRepository;
        this.bqQueryBuilder = bqQueryBuilder;
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
}
