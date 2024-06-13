package com.googleTrendsBigQuery.googleTrendsRestApis.service;

import org.springframework.stereotype.Service;

@Service
public interface TopTermsService {
    Long saveDataFromBQtoMySQL();
}
