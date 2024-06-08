package com.googleTrendsBigQuery.googleTrendsRestApis.service;

import com.googleTrendsBigQuery.googleTrendsRestApis.payload.InternationalTermDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TopInternationalTermsService {
    List<InternationalTermDetails> getTopInternationalTermDetails();
}
