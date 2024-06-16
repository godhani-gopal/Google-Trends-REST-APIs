package com.googleTrendsBigQuery.googleTrendsRestApis.repository;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopRisingTerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface InternationalTopRisingTermsRepository extends JpaRepository<InternationalTopRisingTerms, Long> {

    @Query("SELECT MAX(itrt.week) FROM InternationalTopRisingTerms itrt")
    Optional<LocalDate> findLatestWeekValue();
}
