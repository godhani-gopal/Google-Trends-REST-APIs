package com.googleTrendsBigQuery.googleTrendsRestApis.payload;

import java.time.LocalDate;
import java.util.Date;

public class InternationalTermDetails {
    String term;
    LocalDate week;
    int score;
    int rank;
    LocalDate refreshDate;
    String countryName;
    String countryCode;
    String regionName;
    String regionCode;

    public InternationalTermDetails() {
    }

    public InternationalTermDetails(String term, LocalDate week, int score, int rank, LocalDate refreshDate, String countryName, String countryCode, String regionName, String regionCode) {
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
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
        return "InternationalTermDetails{" + "term='" + term + '\'' + ", week='" + week + '\'' + ", score=" + score + ", rank=" + rank + ", refreshDate=" + refreshDate + ", countryName='" + countryName + '\'' + ", countryCode='" + countryCode + '\'' + ", regionName='" + regionName + '\'' + ", regionCode='" + regionCode + '\'' + '}';
    }
}
