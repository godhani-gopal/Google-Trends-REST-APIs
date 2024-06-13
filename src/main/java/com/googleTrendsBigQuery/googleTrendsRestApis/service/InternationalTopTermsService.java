package com.googleTrendsBigQuery.googleTrendsRestApis.service;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopTerms;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface InternationalTopTermsService {

    Long saveDataFromBQtoMySQL();
}
