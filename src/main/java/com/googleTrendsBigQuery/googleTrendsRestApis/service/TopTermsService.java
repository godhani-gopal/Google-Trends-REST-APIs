package com.googleTrendsBigQuery.googleTrendsRestApis.service;

import com.googleTrendsBigQuery.googleTrendsRestApis.payload.TermAnalysis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface TopTermsService {

    Page<?> getTopTerms(
                        String term,
                        String dmaName,
                        String dmaId,
                        String week,
                        Integer rank,
                        Integer score,
                        Pageable pageable);

    Long saveDataFromBQtoMySQL();

    TermAnalysis getPredictiveInsights(Object internationalTopRisingTerms);
}