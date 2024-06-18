package com.googleTrendsBigQuery.googleTrendsRestApis.service;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.TopTerms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public interface TopTermsService {

    Page<TopTerms> getTopTerms(String dmaName, String dmaId, String week, Integer rank, Integer score,Integer page,Integer pageSize, Pageable pageable);

    Long saveDataFromBQtoMySQL();

}