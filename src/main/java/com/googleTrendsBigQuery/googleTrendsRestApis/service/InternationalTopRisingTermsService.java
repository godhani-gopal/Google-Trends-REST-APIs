package com.googleTrendsBigQuery.googleTrendsRestApis.service;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopRisingTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.payload.TermAnalysis;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface InternationalTopRisingTermsService {

    Long saveDataFromBQtoMySQL();

    LocalDate findLatestWeekValue();

    Long saveLatestDataFromBQtoMySQL();

    TermAnalysis getPredictiveInsights(InternationalTopRisingTerms internationalTopRisingTerms);

    PagedModel<InternationalTopRisingTerms> getTerms(String term, Integer percentGain, LocalDate week, Integer score, Integer rank, LocalDate refreshDate, String countryName, String countryCode, String regionName, String regionCode, Integer numOfTerms, Pageable pageable);
}
