package com.googleTrendsBigQuery.googleTrendsRestApis.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public interface QueryBuilder {
    String getTopRisingTermsInternationalQuery(String countryName, int limit);

    String getTopInternationalTermsQuery(LocalDate week, Integer score, Integer rank, LocalDate refreshDate, String countryName, String countryCode, String regionName, String regionCode, Integer numOfRecords);

    String findMostRelevantWeekValueQuery(String tableName, LocalDate week);

    String findMostRelevantRefreshDateValueQuery(String tableName, LocalDate refreshDate);

    String loadDataFromTopTermsQuery();

    String loadDataFromTopRisingTermsQuery();

    String loadDataFromInternationalTopTermsQuery();

    String loadDataFromInternationalTopRisingTermsQuery();
}