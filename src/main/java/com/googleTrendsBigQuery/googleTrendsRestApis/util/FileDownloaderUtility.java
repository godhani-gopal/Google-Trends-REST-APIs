package com.googleTrendsBigQuery.googleTrendsRestApis.util;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDownloaderUtility {

    private void downloadFile(String fileRemoteUrl, String relativePath, String localFileName) {
        try (InputStream in = new URL(fileRemoteUrl).openStream()) {
            Path targetPath = Paths.get(System.getProperty("user.dir") + relativePath).resolve(localFileName);
            try (FileOutputStream out = new FileOutputStream(targetPath.toFile())) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to download file", e);
        }
    }
}