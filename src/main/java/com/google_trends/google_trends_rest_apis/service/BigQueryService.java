package com.google_trends.google_trends_rest_apis.service;

import com.google.cloud.bigquery.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BigQueryService {

    private final BigQuery bigQuery;

    @Autowired
    public BigQueryService(BigQuery bigQuery) {
        this.bigQuery = bigQuery;
    }

    public List<String> getTopTermsFromCountry(String countryName) throws InterruptedException {
        final String query = "SELECT distinct(term) FROM `bigquery-public-data.google_trends.international_top_rising_terms` WHERE country_name LIKE '%" + countryName + "%' LIMIT 20;";

        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();
        Job queryJob = bigQuery.create(JobInfo.newBuilder(queryConfig).build());
        queryJob = queryJob.waitFor();

        if (queryJob == null) {
            throw new RuntimeException("Job no longer exists!");
        }

        if (queryJob.getStatus().getError() != null) {
            throw new RuntimeException(queryJob.getStatus().getError().toString());
        }

        TableResult result = queryJob.getQueryResults();
        List<String> topTerms = new ArrayList<>();

        for (FieldValueList row : result.iterateAll()) {
            topTerms.add(row.get("term").getStringValue());
        }

        return topTerms;
    }
}
