package com.mb.lab.banks.utils.storage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface StorageProvider {

    public String store(File photo, String path, String filename) throws IOException;

    public List<String> store(List<File> photos, String path, List<String> filenames) throws IOException;

    public void remove(String path, String filename);

    public void remove(String path, List<String> filenames);

    public String getUrl(String path, String filename);

    public File getFile(String path, String filename);

}
