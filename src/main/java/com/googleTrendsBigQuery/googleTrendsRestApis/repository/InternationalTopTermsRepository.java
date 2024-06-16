package com.googleTrendsBigQuery.googleTrendsRestApis.repository;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopTerms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InternationalTopTermsRepository extends JpaRepository<InternationalTopTerms, Long> {


    @Query("SELECT MAX(itt.week) FROM InternationalTopTerms itt")
    Optional<LocalDate> findLatestWeekValue();

    @Query("SELECT itt FROM InternationalTopTerms itt " +
            "WHERE (:week IS NULL OR itt.week = :week) " +
            "AND (:score IS NULL OR itt.score = :score) " +
            "AND (:rank IS NULL OR itt.rank = :rank) " +
            "AND (:refreshDate IS NULL OR itt.refreshDate = :refreshDate) " +
            "AND (:countryName IS NULL OR itt.countryName = :countryName) " +
            "AND (:countryCode IS NULL OR itt.countryCode = :countryCode) " +
            "AND (:regionName IS NULL OR itt.regionName = :regionName) " +
            "AND (:regionCode IS NULL OR itt.regionCode = :regionCode) " +
            "AND (:term IS NULL OR lower(itt.term) LIKE lower(concat('%', :term, '%')))")
    Page<InternationalTopTerms> findTerms(
            @Param("term") String term,
            @Param("week") LocalDate week,
            @Param("score") Integer score,
            @Param("rank") Integer rank,
            @Param("refreshDate") LocalDate refreshDate,
            @Param("countryName") String countryName,
            @Param("countryCode") String countryCode,
            @Param("regionName") String regionName,
            @Param("regionCode") String regionCode,
            Pageable pageable);

    @Query("SELECT DISTINCT itt.term FROM InternationalTopTerms itt " +
            " WHERE (:term IS NULL OR lower(itt.term) LIKE lower(concat('%', :term, '%')))")
    List<String> searchTerms(String term);
}