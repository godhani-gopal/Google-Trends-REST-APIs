package com.googleTrendsBigQuery.googleTrendsRestApis.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "international_top_rising_terms")
public class InternationalTopRisingTerms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String term;
    private Integer percentGain;
    private LocalDate week;
    private Integer score;
    private Integer rank;
    private LocalDate refreshDate;
    private String countryName;
    private String countryCode;
    private String regionName;
    private String regionCode;

    public InternationalTopRisingTerms() {
    }

    public InternationalTopRisingTerms(Long id, String term, Integer percentGain, LocalDate week, Integer score, Integer rank, LocalDate refreshDate, String countryName, String countryCode, String regionName, String regionCode) {
        this.id = id;
        this.term = term;
        this.percentGain = percentGain;
        this.week = week;
        this.score = score;
        this.rank = rank;
        this.refreshDate = refreshDate;
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.regionName = regionName;
        this.regionCode = regionCode;
    }

    public Long getId() {
        return id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Integer getPercentGain() {
        return percentGain;
    }

    public void setPercentGain(Integer percentGain) {
        this.percentGain = percentGain;
    }

    public LocalDate getWeek() {
        return week;
    }

    public void setWeek(LocalDate week) {
        this.week = week;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public LocalDate getRefreshDate() {
        return refreshDate;
    }

    public void setRefreshDate(LocalDate refreshDate) {
        this.refreshDate = refreshDate;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }
}
