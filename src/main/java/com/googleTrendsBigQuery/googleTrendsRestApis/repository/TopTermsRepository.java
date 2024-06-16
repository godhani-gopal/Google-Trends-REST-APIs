package com.googleTrendsBigQuery.googleTrendsRestApis.repository;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.TopTerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TopTermsRepository extends JpaRepository<TopTerms, Long> {

    @Query("SELECT MAX(tt.week) FROM TopTerms tt")
    Optional<LocalDate> findLatestWeekValue();
}
