package com.googleTrendsBigQuery.googleTrendsRestApis.service;

import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    String getJwtToken(String code);

    String getProfileDetailsGoogle(String accessToken);
}
