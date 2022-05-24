package com.mb.lab.banks.utils.common;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    public static final BytesHolder convertFromBase64(String base64String) {
        if (StringUtils.isEmpty(base64String)) {
            return null;
        }

        String[] splitedStrings = base64String.split(",");
        if (splitedStrings.length != 2) {
            LOGGER.error("Cannot convert to bytes: Invalid format");
            return null;
        }

        if (!splitedStrings[0].startsWith("data:") || !splitedStrings[0].endsWith(";base64")) {
            LOGGER.error("Cannot convert to bytes: Missing header");
            return null;
        }

        String mimeType = splitedStrings[0].substring(5, splitedStrings[0].length() - 7);
        byte[] fileBytes = null;
        try {
            fileBytes = Base64.getDecoder().decode(splitedStrings[1]);
        } catch (Exception e) {
            LOGGER.error("Cannot decode Base64 to bytes", e);
            return null;
        }
        
        return new BytesHolder(fileBytes, mimeType);
    }

    public static class BytesHolder {

        private byte[] fileBytes;
        private String mimeType;

        public BytesHolder(byte[] fileBytes, String mimeType) {
            super();
            this.fileBytes = fileBytes;
            this.mimeType = mimeType;
        }

        public byte[] getFileBytes() {
            return fileBytes;
        }

        public void setFileBytes(byte[] fileBytes) {
            this.fileBytes = fileBytes;
        }

        public String getMimeType() {
            return mimeType;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }

    }

}
