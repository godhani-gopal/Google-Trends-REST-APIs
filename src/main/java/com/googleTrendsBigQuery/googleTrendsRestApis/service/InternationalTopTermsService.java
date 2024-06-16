package com.googleTrendsBigQuery.googleTrendsRestApis.service;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.payload.TermAnalysis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface InternationalTopTermsService {

    Long saveDataFromBQtoMySQL();

    Long saveLatestDataFromBQtoMySQL();

    LocalDate findLatestWeekValue();

    Page<InternationalTopTerms> findTerms(String term, LocalDate week, Integer score, Integer rank, LocalDate refreshDate, String countryName, String countryCode, String regionName, String regionCode, Integer numOfTerms, Pageable pageable);

    List<String> getMatchingTerms(String term);

    TermAnalysis getPredictiveInsights(InternationalTopTerms internationalTopTerms);
}
