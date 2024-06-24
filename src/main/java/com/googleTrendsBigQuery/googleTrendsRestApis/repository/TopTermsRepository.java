package com.googleTrendsBigQuery.googleTrendsRestApis.repository;

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
public interface TopTermsRepository extends JpaRepository<TopTerms, Long>, JpaSpecificationExecutor<TopTerms> {

    @Query("SELECT t FROM TopTerms t WHERE t.week = :week OR t.week = (SELECT MIN(t2.week) FROM TopTerms t2 WHERE t2.week >= :week)")
    List<TopTerms> findByNearestWeek(@Param("week") LocalDate week, Pageable pageable);

    @Query("SELECT MAX(tt.week) from TopTerms tt")
    Optional<LocalDate> findLatestWeekValue();
}
