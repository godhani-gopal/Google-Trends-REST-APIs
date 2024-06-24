package com.googleTrendsBigQuery.googleTrendsRestApis.service.serviceImpl;

import com.googleTrendsBigQuery.googleTrendsRestApis.service.AuthService;
import com.googleTrendsBigQuery.googleTrendsRestApis.util.GoogleJwtProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    GoogleJwtProvider googleJwtProvider;

    public AuthServiceImpl(GoogleJwtProvider googleJwtProvider) {
        this.googleJwtProvider = googleJwtProvider;
    }

    @Override
    public String getJwtToken(String code) {
       return googleJwtProvider.getOauthIdTokenGoogle(code);
    }

    @Override
    public String getProfileDetailsGoogle(String accessToken) {
        return googleJwtProvider.getGoogleProfileDetails(accessToken);
    }
}