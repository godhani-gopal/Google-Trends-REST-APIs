package com.googleTrendsBigQuery.googleTrendsRestApis.service;

import com.googleTrendsBigQuery.googleTrendsRestApis.payload.TermAnalysis;
import org.springframework.stereotype.Service;

@Service
public interface AIService {
    <T> TermAnalysis getAIResults(T input);
}
