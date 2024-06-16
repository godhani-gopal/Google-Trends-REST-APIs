package com.googleTrendsBigQuery.googleTrendsRestApis.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "international_top_terms")
public class InternationalTopTerms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    @NotNull
    private String term;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    @NotNull
    private LocalDate week;

    private Integer score;

    @Min(value = 1)
    @Max(value = 25)
    @NotNull
    @Column(nullable = false)
    private Integer rank;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate refreshDate;

    @Column(nullable = false)
    @NotNull
    private String countryName;

    @Column(nullable = false)
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{2}$", message = "Country code must be exactly two alphabetical characters")
    private String countryCode;

    @Column(nullable = false)
    @NotNull
    private String regionName;

    @Column(nullable = false)
    @NotNull
    private String regionCode;

    public InternationalTopTerms() {
    }

    public InternationalTopTerms(String term, LocalDate week, Integer score, Integer rank, LocalDate refreshDate, String countryName, String countryCode, String regionName, String regionCode) {
        this.term = term;
        this.week = week;
        this.score = score;
        this.rank = rank;
        this.refreshDate = refreshDate;
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.regionName = regionName;
        this.regionCode = regionCode;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
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

    @Override
    public String toString() {
        return "InternationalTermDetails{" + "term='" + term + '\'' + ", week=" + week + ", score=" + score + ", rank=" + rank + ", refreshDate=" + refreshDate + ", countryName='" + countryName + '\'' + ", countryCode='" + countryCode + '\'' + ", regionName='" + regionName + '\'' + ", regionCode='" + regionCode + '\'' + '}';
    }
}
