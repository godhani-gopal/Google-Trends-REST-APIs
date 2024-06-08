package com.google_trends.google_trends_rest_apis.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class GCPConfig {
    @Value("${gcp.project.id}")
    private String projectId;

    @Value("${gcp.service-account.credentialsFilePath}")
    private String credentialsFilePath;

    @Bean
    public BigQuery bigQuery() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsFilePath));
        return BigQueryOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build().getService();
    }
}