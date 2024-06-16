package com.googleTrendsBigQuery.googleTrendsRestApis.service.serviceImpl;

import com.google.cloud.bigquery.FieldValueList;
import com.googleTrendsBigQuery.googleTrendsRestApis.entity.TopRisingTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.exception.ResourceNotFoundException;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.BQRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.TopRisingTermsRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.TopRisingTermsService;
import com.googleTrendsBigQuery.googleTrendsRestApis.util.QueryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.googleTrendsBigQuery.googleTrendsRestApis.util.BQFieldValueUtil.*;

@Service
public class TopRisingTermsServiceImpl implements TopRisingTermsService {

    TopRisingTermsRepository topRisingTermsRepository;
    BQRepository bqRepository;
    QueryBuilder bqQueryBuilder;

    public TopRisingTermsServiceImpl(TopRisingTermsRepository topRisingTermsRepository,
                                     BQRepository bqRepository,
                                     @Qualifier("bqQueryBuilder") QueryBuilder bqQueryBuilder) {
        this.topRisingTermsRepository = topRisingTermsRepository;
        this.bqRepository = bqRepository;
        this.bqQueryBuilder = bqQueryBuilder;
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
    public Long saveLatestDataFromBQtoMySQL() {
        return bqRepository.saveDataFromBQtoMySQL(
                () -> bqQueryBuilder.loadLatestDataFromTopRisingTermsQuery(this.findLatestWeekValue()),
                this::mapToTopRisingTerms, ((batch, totalNumberOfSavedRecords) -> {
                    topRisingTermsRepository.saveAll(batch);
                    totalNumberOfSavedRecords.addAndGet(batch.size());
                }));
    }

    @Override
    public LocalDate findLatestWeekValue() {
        return topRisingTermsRepository.findLatestWeekValue().orElseThrow(() -> new ResourceNotFoundException("week", "MAX(week)", "latest week cannot be found"));
    }
}
