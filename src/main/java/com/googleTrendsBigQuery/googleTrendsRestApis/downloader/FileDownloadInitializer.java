package com.googleTrendsBigQuery.googleTrendsRestApis.downloader;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileDownloadInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment env = applicationContext.getEnvironment();
        String fileDownloadUrl = env.getProperty("file.download.url");
        String fileDownloadLocation = env.getProperty("file.download.location");

        try {
            downloadFileIfNotExists(fileDownloadUrl, fileDownloadLocation);
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file", e);
        }
    }

    private void downloadFileIfNotExists(String fileURL, String localFilePath) throws IOException {
        if (Files.notExists(Paths.get(localFilePath))) {
            try (InputStream in = new URL(fileURL).openStream()) {
                Files.copy(in, Paths.get(localFilePath), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}
