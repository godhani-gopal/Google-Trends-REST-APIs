package com.googleTrendsBigQuery.googleTrendsRestApis.repository;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopRisingTerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternationalTopRisingTermsRepository extends JpaRepository<InternationalTopRisingTerms, Long> {
}
