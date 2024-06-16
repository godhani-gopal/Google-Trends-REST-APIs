package com.googleTrendsBigQuery.googleTrendsRestApis.service.serviceImpl;

import com.google.cloud.bigquery.FieldValueList;
import com.googleTrendsBigQuery.googleTrendsRestApis.entity.TopTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.exception.ResourceNotFoundException;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.BQRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.TopTermsRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.TopTermsService;
import com.googleTrendsBigQuery.googleTrendsRestApis.util.QueryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.googleTrendsBigQuery.googleTrendsRestApis.util.BQFieldValueUtil.*;

@Service
public class TopTermsServiceImpl implements TopTermsService {

    TopTermsRepository topTermsRepository;
    BQRepository bqRepository;
    QueryBuilder bqQueryBuilder;

    public TopTermsServiceImpl(TopTermsRepository topTermsRepository,
                               BQRepository bqRepository,
                               @Qualifier("bqQueryBuilder") QueryBuilder bqQueryBuilder) {
        this.topTermsRepository = topTermsRepository;
        this.bqRepository = bqRepository;
        this.bqQueryBuilder = bqQueryBuilder;
    }

    @Override
    public Long saveDataFromBQtoMySQL() {
        return bqRepository.saveDataFromBQtoMySQL(
                bqQueryBuilder::loadDataFromTopTermsQuery,
                this::mapToTopTerms,
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

    @Override
    public Long saveLatestDataFromBQtoMySQL() {
        return bqRepository.saveDataFromBQtoMySQL(
                () -> bqQueryBuilder.loadLatestDataFromTopTermsQuery(this.findLatestWeekValue()),
                this::mapToTopTerms, ((batch, totalNumberOfSavedRecords) -> {
                    topTermsRepository.saveAll(batch);
                    totalNumberOfSavedRecords.addAndGet(batch.size());
                }));
    }

    @Override
    public LocalDate findLatestWeekValue() {
        return topTermsRepository.findLatestWeekValue().orElseThrow(() -> new ResourceNotFoundException("week", "MAX(week)", "latest week cannot be found"));
    }
}