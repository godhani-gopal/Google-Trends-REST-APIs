package com.googleTrendsBigQuery.googleTrendsRestApis.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.googleTrendsBigQuery.googleTrendsRestApis.util.BigQueryExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;

@Configuration
public class GCPConfig {

    @Value("${gcp.project.id}")
    private String projectId;

    @Value("${gcp.service-account.credentials}")
    private String gcpCredentials;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Bean
    public BigQuery bigQuery() throws IOException {
        Path path = Path.of(System.getProperty("user.home") + "/" + gcpCredentials);
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(path.toFile()));
        return BigQueryOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build().getService();
    }

    @Bean
    public BigQueryExecutor bigQueryExecutor(BigQuery bigQuery) {
        return new BigQueryExecutor(bigQuery);
    }

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

}