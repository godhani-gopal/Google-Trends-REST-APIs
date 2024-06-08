package com.googleTrendsBigQuery.googleTrendsRestApis.service.serviceImpl;

import com.googleTrendsBigQuery.googleTrendsRestApis.payload.InternationalTermDetails;
import com.googleTrendsBigQuery.googleTrendsRestApis.repository.TopInternationalTermsRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.TopInternationalTermsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TopInternationalTermsServiceImpl implements TopInternationalTermsService {

    TopInternationalTermsRepository topInternationalTermsRepository;

    @Override
    public List<InternationalTermDetails> getTopInternationalTermDetails() {
        return List.of(new InternationalTermDetails("Search Term", LocalDate.now(), 100, 1, LocalDate.now(), "IndiaCountry", "CC1", "Region 1", "RC1"));
    }
}
