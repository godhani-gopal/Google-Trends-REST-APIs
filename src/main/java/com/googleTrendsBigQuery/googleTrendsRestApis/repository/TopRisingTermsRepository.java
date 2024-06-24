package com.googleTrendsBigQuery.googleTrendsRestApis.repository;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.TopRisingTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.entity.TopTerms;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TopRisingTermsRepository extends JpaRepository<TopRisingTerms, Long>,JpaSpecificationExecutor<TopRisingTerms> {

    @Query("SELECT MAX(tst.week) from TopRisingTerms tst")
    Optional<LocalDate> findLatestWeekValue();

    @Query("SELECT t FROM TopRisingTerms t WHERE t.week = :week OR t.week = (SELECT MIN(t2.week) FROM TopRisingTerms t2 WHERE t2.week >= :week)")
    List<TopRisingTerms> findByNearestWeek(@Param("week") LocalDate week, Pageable pageable);
}