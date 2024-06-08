package com.googleTrendsBigQuery.googleTrendsRestApis.service.serviceImpl;

import com.googleTrendsBigQuery.googleTrendsRestApis.repository.TopRisingInternationalTermsRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.TopRisingInternationalTermsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopRisingInternationalTermsServiceImpl implements TopRisingInternationalTermsService {

    private final TopRisingInternationalTermsRepository topRisingInternationalTermsRepository;

    public TopRisingInternationalTermsServiceImpl(TopRisingInternationalTermsRepository topRisingInternationalTermsRepository) {
        this.topRisingInternationalTermsRepository = topRisingInternationalTermsRepository;
    }

    public List<String> getTopTermsByCountry(String countryName, int limit) throws InterruptedException {
        return topRisingInternationalTermsRepository.getTopRisingTermsByCountry(countryName, limit);
    }
}
