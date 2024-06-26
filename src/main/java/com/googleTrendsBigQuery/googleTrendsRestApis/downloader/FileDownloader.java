package com.googleTrendsBigQuery.googleTrendsRestApis.downloader;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileDownloader {

    public void downloadFile(String fileUrl, String destinationFilePath) throws IOException {
        Path destinationPath = Paths.get(destinationFilePath);

        // Check if the file already exists
        if (Files.exists(destinationPath)) {
            // Replace the existing file
            Files.delete(destinationPath);
        }

        try (InputStream in = new URL(fileUrl).openStream()) {
            Files.copy(in, destinationPath);
            System.out.println("File downloaded successfully to " + destinationFilePath);
        } catch (IOException e) {
            System.err.println("Failed to download file from " + fileUrl);
            throw e;
        }
    }
}
