package com.googleTrendsBigQuery.googleTrendsRestApis.repository;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.TopRisingTerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TopRisingTermsRepository extends JpaRepository<TopRisingTerms, Long> {

    @Query("SELECT MAX(tst.week) from TopRisingTerms tst")
    Optional<LocalDate> findLatestWeekValue();
}