package com.googleTrendsBigQuery.googleTrendsRestApis.service.serviceImpl;

import com.googleTrendsBigQuery.googleTrendsRestApis.repository.InternationalTopRisingTermsRepository;
import com.googleTrendsBigQuery.googleTrendsRestApis.service.InternationalTopRisingTermsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternationalTopRisingTermsServiceImpl implements InternationalTopRisingTermsService {

    private final InternationalTopRisingTermsRepository internationalTopRisingTermsRepository;

    public InternationalTopRisingTermsServiceImpl(InternationalTopRisingTermsRepository internationalTopRisingTermsRepository) {
        this.internationalTopRisingTermsRepository = internationalTopRisingTermsRepository;
    }

    public List<String> getTopTermsByCountry(String countryName, int limit) throws InterruptedException {
        return internationalTopRisingTermsRepository.getTopRisingTermsByCountry(countryName, limit);
    }
}
