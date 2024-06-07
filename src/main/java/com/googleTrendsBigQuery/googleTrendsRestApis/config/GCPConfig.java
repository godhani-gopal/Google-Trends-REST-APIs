package com.googleTrendsBigQuery.googleTrendsRestApis.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

@Configuration
public class GCPConfig {

    @Value("${gcp.project.id}")
    private String projectId;

    @Value("${gcp.service-account.credentials}")
    private String gcpCredentials;

    @Bean
    public BigQuery bigQuery() throws IOException {
        Path path = Path.of(System.getProperty("user.home") +"/"+ gcpCredentials);
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(path.toFile()));
        return BigQueryOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build().getService();
    }
}