package com.googleTrendsBigQuery.googleTrendsRestApis.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface TopRisingTermsService {
    Long saveDataFromBQtoMySQL();

    Long saveLatestDataFromBQtoMySQL();

    LocalDate findLatestWeekValue();
}
