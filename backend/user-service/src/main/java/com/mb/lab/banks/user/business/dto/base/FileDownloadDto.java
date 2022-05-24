package com.mb.lab.banks.user.business.dto.base;

import java.io.File;

public class FileDownloadDto {

    private String fileName;
    private File file;

    public FileDownloadDto(String fileName, File file) {
        super();
        this.fileName = fileName;
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }

}
