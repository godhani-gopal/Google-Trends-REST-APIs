package com.googleTrendsBigQuery.googleTrendsRestApis.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "top_rising_terms")
public class TopRisingTerms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String term;
    private Integer percentGain;
    private LocalDate week;
    private Integer score;
    private Integer rank;
    private LocalDate refreshDate;
    private String dmaName;
    private String dmaId;

    public TopRisingTerms() {
    }

    public TopRisingTerms(String term, Integer percentGain, LocalDate week, Integer score, Integer rank, LocalDate refreshDate, String dmaName, String dmaId) {
        this.id = id;
        this.term = term;
        this.percentGain = percentGain;
        this.week = week;
        this.score = score;
        this.rank = rank;
        this.refreshDate = refreshDate;
        this.dmaName = dmaName;
        this.dmaId = dmaId;
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

    public String getDmaName() {
        return dmaName;
    }

    public void setDmaName(String dmaName) {
        this.dmaName = dmaName;
    }

    public String getDmaId() {
        return dmaId;
    }

    public void setDmaId(String dmaId) {
        this.dmaId = dmaId;
    }
}
