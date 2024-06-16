package com.googleTrendsBigQuery.googleTrendsRestApis.service;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopRisingTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.payload.TermAnalysis;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface InternationalTopRisingTermsService {

    Long saveDataFromBQtoMySQL();

    LocalDate findLatestWeekValue();

    Long saveLatestDataFromBQtoMySQL();

    TermAnalysis getPredictiveInsights(InternationalTopRisingTerms internationalTopRisingTerms);
}
