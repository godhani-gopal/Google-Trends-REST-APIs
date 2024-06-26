package com.googleTrendsBigQuery.googleTrendsRestApis.repository;

import com.googleTrendsBigQuery.googleTrendsRestApis.entity.InternationalTopRisingTerms;
import com.googleTrendsBigQuery.googleTrendsRestApis.entity.TopTerms;
import org.springframework.data.domain.Page;
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
public interface InternationalTopRisingTermsRepository extends JpaRepository<InternationalTopRisingTerms, Long> {

    @Query("SELECT MAX(itrt.week) FROM InternationalTopRisingTerms itrt")
    Optional<LocalDate> findLatestWeekValue();

    @Query("SELECT itrt FROM InternationalTopRisingTerms itrt " +
            "WHERE (:week IS NULL OR itrt.week = :week) " +
            "AND (:score IS NULL OR itrt.score = :score) " +
            "AND (:rank IS NULL OR itrt.rank = :rank) " +
            "AND (:refreshDate IS NULL OR itrt.refreshDate = :refreshDate) " +
            "AND (:countryName IS NULL OR lower(itrt.countryName) LIKE lower(concat('%', :countryName, '%'))) " +
            "AND (:countryCode IS NULL OR itrt.countryCode = :countryCode) " +
            "AND (:regionName IS NULL OR lower(itrt.regionName) LIKE lower(concat('%', :regionName, '%'))) " +
            "AND (:regionCode IS NULL OR itrt.regionCode = :regionCode) " +
            "AND (:term IS NULL OR lower(itrt.term) LIKE lower(concat('%', :term, '%'))) " +
            "AND (:percentGain IS NULL OR itrt.percentGain = :percentGain) ")
    Page<InternationalTopRisingTerms> findTerms(
            @Param("term") String term,
            @Param("percentGain") Integer percentGain,
            @Param("week") LocalDate week,
            @Param("score") Integer score,
            @Param("rank") Integer rank,
            @Param("refreshDate") LocalDate refreshDate,
            @Param("countryName") String countryName,
            @Param("countryCode") String countryCode,
            @Param("regionName") String regionName,
            @Param("regionCode") String regionCode,
            Pageable pageable);

    boolean existsByTermAndPercentGainAndWeekAndCountryNameAndCountryCodeAndRegionNameAndRegionCode(
            String term, Integer percentGain, LocalDate week,
            String countryName, String countryCode,
            String regionName, String regionCode);

}
