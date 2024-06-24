package com.googleTrendsBigQuery.googleTrendsRestApis.service;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.TopRisingTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.payload.TermAnalysis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface TopRisingTermsService {

    Long saveDataFromBQtoMySQL();

    Long saveLatestDataFromBQtoMySQL();

    LocalDate findLatestWeekValue();

    Page<?> getTopRisingTerms(
            String term,
            String dmaName,
            String dmaId,
            String week,
            Integer rank,
            Integer score,
            Integer percentGain,
            Pageable pageable);

    TermAnalysis getPredictiveInsights(TopRisingTerms topRisingTerms);
}
