package com.mb.lab.banks.utils.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class FileStorageProvider implements StorageProvider {

    private String rootPath;
    private String rootUrl;

    public FileStorageProvider(String rootPath, String rootUrl) {
        this.rootPath = rootPath;
        this.rootUrl = rootUrl;
    }

    @Override
    public String store(File photo, String path, String filename) throws IOException {
        FileInputStream photoInputStream = null;
        try {
            photoInputStream = new FileInputStream(photo);

            String fileAbsolutePath = rootPath + path + filename;
            File targetFile = new File(fileAbsolutePath);
            targetFile.mkdirs();

            Files.copy(photoInputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return filename;
        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(photoInputStream);
        }
    }

    @Override
    public List<String> store(List<File> photos, String path, List<String> filenames) throws IOException {
        // TODO: May we using multiple thread to write?
        for (int i = 0; i < photos.size(); i++) {
            store(photos.get(i), path, filenames.get(i));
        }
        return filenames;
    }

    @Override
    public void remove(String path, String filename) {
        File targetFile = new File(rootPath + path + filename);
        targetFile.delete();
    }

    @Override
    public void remove(String path, List<String> filenames) {
        for (String filename : filenames) {
            remove(path, filename);
        }
    }

    @Override
    public String getUrl(String path, String filename) {
        return rootUrl + path + filename;
    }

    @Override
    public File getFile(String path, String filename) {
        return new File(rootPath + path + filename);
    }

}
