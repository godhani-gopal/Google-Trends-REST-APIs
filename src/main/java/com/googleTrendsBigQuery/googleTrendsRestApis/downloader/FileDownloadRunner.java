package com.googleTrendsBigQuery.googleTrendsRestApis.downloader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FileDownloadRunner implements CommandLineRunner {

    private final FileDownloader fileDownloader;
    private final String fileDownloadUrl;
    private final String fileDownloadLocation;

    @Autowired
    public FileDownloadRunner(FileDownloader fileDownloader,
                              @Value("${file.download.url}") String fileDownloadUrl,
                              @Value("${file.download.location}") String fileDownloadLocation) {
        this.fileDownloader = fileDownloader;
        this.fileDownloadUrl = fileDownloadUrl;
        this.fileDownloadLocation = fileDownloadLocation;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            fileDownloader.downloadFile(fileDownloadUrl, fileDownloadLocation);
        } catch (Exception e) {
            System.err.println("Failed to download file: " + e.getMessage());
            throw e;
        }
    }
}
