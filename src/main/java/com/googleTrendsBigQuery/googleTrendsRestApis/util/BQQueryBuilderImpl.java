package com.googleTrendsBigQuery.googleTrendsRestApis.util;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Qualifier("bqQueryBuilder")
public class BQQueryBuilderImpl implements QueryBuilder {
    @Override
    public String getTopRisingTermsInternationalQuery(String countryName, int limit) {
        String query = "";
        try {
            query = "SELECT distinct(term) FROM `bigquery-public-data.google_trends.international_top_rising_terms`";
            if (!(countryName == null || countryName.isEmpty())) {
                query = query + " where country_name like '%" + countryName + "%'";
            }
            if (limit > 25 || limit <= 0) {
                throw new Exception(("Invalid Input! Maximum 25 records available!"));
            } else {
                query = query + " limit " + limit;
            }
        } catch (Exception e) {
            System.out.println("Invalid Input entered. Cannot Generate Query.");
        }
        return query;
    }

    @Override
    public String getTopInternationalTermsQuery(LocalDate week, Integer score, Integer rank, LocalDate refreshDate, String countryName, String countryCode, String regionName, String regionCode, Integer numOfRecords) {
        StringBuilder query = new StringBuilder("SELECT distinct(term) FROM `bigquery-public-data.google_trends.international_terms` WHERE 1=1");

        if (week != null) {
            query.append(" AND week=").append(week);
        }

        if (score != null && rank != null) {
            query.append(" AND (score=").append(score).append(" OR rank=").append(rank).append(")");
        } else if (score != null) {
            query.append(" AND score=").append(score);
        } else if (rank != null) {
            query.append(" AND rank=").append(rank);
        }

        if (refreshDate != null) {
            query.append(" AND refresh_date=").append(refreshDate);
        }

        if (countryName != null && countryCode != null) {
            query.append(" AND (country_name LIKE '%").append(countryName).append("%' OR country_code LIKE '%").append(countryCode).append("%')");
        } else if (countryName != null) {
            query.append(" AND country_name LIKE '%").append(countryName).append("%'");
        } else if (countryCode != null) {
            query.append(" AND country_code LIKE '%").append(countryCode).append("%'");
        }

        if (regionName != null && regionCode != null) {
            query.append(" AND (region_name LIKE '%").append(countryName).append("%' OR region_code LIKE '%").append(countryCode).append("%')");
        } else if (regionName != null) {
            query.append(" AND region_name LIKE '%").append(countryName).append("%'");
        } else if (regionCode != null) {
            query.append(" AND region_code LIKE '%").append(countryCode).append("%'");
        }

        query.append(" ORDER BY week DESC");
        if (numOfRecords != null) {
            query.append(" limit ").append(numOfRecords);
        }

        query.append(";");
        return query.toString();
    }

    @Override
    public String findMostRelevantWeekValueQuery(String tableName, LocalDate week) {
        StringBuilder query = new StringBuilder();

        if (week == null)
            return query.append("SELECT MAX(week) AS week FROM `bigquery-public-data.google_trends.").append(tableName).append("`;").toString();
        else
            return query.append("SELECT week FROM `bigquery-public-data.google_trends.").append(tableName).append("`. ORDER BY ABS(DATE_DIFF(week, '").append(week).append("', WEEK)) LIMIT 1;").toString();
    }

    @Override
    public String findMostRelevantRefreshDateValueQuery(String tableName, LocalDate refreshDate) {
        StringBuilder query = new StringBuilder();

        if (refreshDate == null)
            return null;
        else
            return query.append("SELECT refresh_date FROM `bigquery-public-data.google_trends.").append(tableName).append("`. ORDER BY ABS(DATE_DIFF(refresh_date, '").append(refreshDate).append("', REFRESH_DATE)) LIMIT 1;").toString();
    }

    @Override
    public String loadDataFromTopTermsQuery() {
        return "SELECT rank, refresh_date, dma_name, dma_id, term, week, score FROM `bigquery-public-data.google_trends.top_terms` ORDER BY week DESC LIMIT 50000;";
    }

    @Override
    public String loadDataFromTopRisingTermsQuery() {
        return "SELECT rank, percent_gain, refresh_date, dma_name, dma_id, term, week, score FROM `bigquery-public-data.google_trends.top_rising_terms` ORDER BY week DESC LIMIT 50000;";
    }


    @Override
    public String loadDataFromInternationalTopTermsQuery() {
        return "SELECT rank, refresh_date, region_name, term, week, score, country_name, country_code, region_code FROM `bigquery-public-data.google_trends.international_top_terms` ORDER BY week DESC LIMIT 50000;";
    }

    @Override
    public String loadDataFromInternationalTopRisingTermsQuery() {
        return "SELECT rank, percent_gain, refresh_date, region_name, term, week, score, country_name, country_code, region_code FROM `bigquery-public-data.google_trends.international_top_rising_terms` ORDER BY week DESC LIMIT 50000;";
    }

    @Override
    public String loadLatestDataFromTopTermsQuery(LocalDate week) {
        return "SELECT rank, refresh_date, dma_name, dma_id, term, week, score FROM `bigquery-public-data.google_trends.top_terms` WHERE week > '" + week.toString() + "';";
    }

    @Override
    public String loadLatestDataFromTopRisingTermsQuery(LocalDate week) {
        return "SELECT rank, percent_gain, refresh_date, dma_name, dma_id, term, week, score FROM `bigquery-public-data.google_trends.top_rising_terms` WHERE week > '" + week.toString() + "';";
    }

    @Override
    public String loadLatestDataFromInternationalTopTermsQuery(LocalDate week) {
        return "SELECT rank, refresh_date, region_name, term, week, score, country_name, country_code, region_code FROM `bigquery-public-data.google_trends.international_top_terms` WHERE week > '" + week.toString() + "';";
    }

    @Override
    public String loadLatestDataFromInternationalTopRisingTermsQuery(LocalDate week) {
        return "SELECT rank, percent_gain, refresh_date, region_name, term, week, score, country_name, country_code, region_code FROM `bigquery-public-data.google_trends.international_top_rising_terms` WHERE week > '" + week.toString() + "';";
    }


}