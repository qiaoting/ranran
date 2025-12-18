package com.ranran.common.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author ranran
 * 文件工具封装
 */
public final class FileUtil {

    public static String getFileNameWithoutExt(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return "";
        }
        File file = new File(filePath);
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 ? fileName.substring(0, lastDotIndex) : fileName;
    }

    public static String getFileExt(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return "";
        }
        File file = new File(filePath);
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 && lastDotIndex < fileName.length() - 1
                ? fileName.substring(lastDotIndex + 1) : "";
    }

    public static String concatFileName(String name, String ext) {
        if (name == null) name = "";
        if (ext == null || ext.isBlank()) return name;
        return name + "." + ext;
    }

    public static boolean createDir(String dirPath) {
        if (dirPath == null || dirPath.isBlank()) {
            return false;
        }
        Path path = Paths.get(dirPath);
        try {
            Files.createDirectories(path);
            return Files.isDirectory(path);
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean isDirExist(String dirPath) {
        if (dirPath == null || dirPath.isBlank()) {
            return false;
        }
        Path path = Paths.get(dirPath);
        return Files.exists(path) && Files.isDirectory(path);
    }

    public static boolean isFileExist(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return false;
        }
        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isRegularFile(path);
    }

    public static boolean createFile(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return false;
        }
        Path path = Paths.get(filePath);
        try {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            return Files.isRegularFile(path);
        }
    }

    public static boolean delete(String path) {
        if (path == null || path.isBlank()) {
            return true;
        }
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            for (File subFile : Objects.requireNonNull(file.listFiles())) {
                delete(subFile.getAbsolutePath());
            }
        }
        return file.delete();
    }

    public static byte[] readBytes(File file) {
        if (file == null) {
            return new byte[0];
        }
        if (!file.exists() || !file.isFile()) {
            return new byte[0];
        }
        if (file.length() == 0) {
            return new byte[0];
        }
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            return new byte[0];
        }
    }
}
