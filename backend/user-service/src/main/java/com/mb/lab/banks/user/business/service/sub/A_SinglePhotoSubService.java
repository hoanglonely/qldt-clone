package com.mb.lab.banks.user.business.service.sub;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.mb.lab.banks.utils.common.ImageUtils;
import com.mb.lab.banks.utils.common.ImageUtils.ImagePosition;
import com.mb.lab.banks.utils.common.StringUtils;
import com.mb.lab.banks.utils.exception.BusinessException;
import com.mb.lab.banks.utils.exception.BusinessExceptionCode;
import com.mb.lab.banks.utils.storage.StorageProvider;

import net.coobird.thumbnailator.Thumbnails;

public abstract class A_SinglePhotoSubService {
    
    private static final String LEGACY_PHOTO_PREFIX = "legacy/";

    @Autowired
    private StorageProvider storageProvider;

    protected abstract String getRelativePath();

    protected abstract int getWidth();

    protected abstract Integer getHeight();

    protected abstract double getPhotoMegabytesLimit();

    protected void checkPhoto(BufferedImage photo) {
        // DO NOTHING
    }

    protected boolean isNeedRemoveTransparent() {
        return true;
    }

    public String storePhoto(MultipartFile multipartFile) throws IOException {
        Assert.notNull(multipartFile, "multipartFile cannot be null");

        boolean isPNG = multipartFile.getContentType() != null && multipartFile.getContentType().toLowerCase().endsWith("png");

        InputStream inputStream = null;
        InputStream photoInputStream = null;
        try {
            // STORE TO TEMP FILE
            File tempFile = File.createTempFile("photo", ".tmp");
            multipartFile.transferTo(tempFile);

            double megabytes = tempFile.length() / (1024 * 1024);
            if (megabytes > getPhotoMegabytesLimit()) {
                throw new BusinessException(BusinessExceptionCode.INVALID_IMAGE_SIZE, "image too big", null);
            }

            photoInputStream = new ByteArrayInputStream(FileUtils.readFileToByteArray(tempFile));

            return storePhoto(photoInputStream, isPNG);
        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(photoInputStream);
        }
    }

    public String storePhoto(InputStream inputStream) throws IOException {
        return storePhoto(inputStream, null, null);
    }

    public String storePhoto(InputStream inputStream, Boolean isPNG) throws IOException {
        return storePhoto(inputStream, isPNG, null);
    }

    public String storePhoto(InputStream inputStream, Boolean isPNG, BigDecimal fixedRatio) throws IOException {
        Assert.notNull(inputStream, "inputStream cannot be null");

        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(inputStream);

            if (bufferedImage == null) {
                throw new BusinessException(BusinessExceptionCode.INVALID_PARAM, "cannot read image");
            }
        } catch (IOException e) {
            throw new BusinessException(BusinessExceptionCode.UNSUPPORTED_IMAGE, "unsupported image", e);
        }

        try {
            // REMOVE TRANSPARENT FOR PHOTO
            if (isNeedRemoveTransparent() && (isPNG == null || isPNG == true)) {
                bufferedImage = ImageUtils.removeTransparent(bufferedImage);
            }

            if (isNeedRemoveTransparent() && (isPNG == null || isPNG == true)) {
                return storeImageToFile(storageProvider, getRelativePath(), null, bufferedImage, getWidth(), getHeight());
            } else {
                return storeImageToFile(storageProvider, getRelativePath(), inputStream, bufferedImage, getWidth(), getHeight());
            }
        } catch (IOException e) {
            throw new BusinessException(BusinessExceptionCode.INVALID_PARAM, "error when store image", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public String storePhoto(File file) throws IOException {
        Assert.notNull(file, "file not null");

        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);

            return storePhoto(inputStream, null);
        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public String storePhoto(String _url) throws IOException {
        Assert.isTrue(!StringUtils.isEmpty(_url), "not null");

        InputStream inputStream = null;
        try {
            URL url = new URL(_url);
            return storePhoto(url.openStream());
        } catch (IOException e) {
            throw new UnsupportedOperationException(e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public void removePhoto(String photo) {
        if (!StringUtils.isEmpty(photo)) {
            if (photo.startsWith(LEGACY_PHOTO_PREFIX)) {
                storageProvider.remove("", photo);
            }
            storageProvider.remove(getRelativePath(), photo);
        }
    }

    public String getPhotoUrl(String photo) {
        if (StringUtils.isEmpty(photo)) {
            return null;
        }
        if (photo.startsWith(LEGACY_PHOTO_PREFIX)) {
            return storageProvider.getUrl("", photo);
        }
        return storageProvider.getUrl(getRelativePath(), photo);
    }

    // PROTECTED
    protected BufferedImage getImageScaled(BufferedImage originalImage, int photoWidth, Integer photoHeight) throws IOException {
        return ImageUtils.getCoverImage(originalImage, photoWidth, photoHeight, ImagePosition.MIDDLE_CENTER);
    }

    // PRIVATE
    private String storeImageToFile(StorageProvider storageProvider,
            String relativePath,
            InputStream inputStream,
            BufferedImage bufferedImage,
            int photoWidth,
            Integer photoHeight) throws IOException {
        BufferedImage bufferedImageScaled = getImageScaled(bufferedImage, photoWidth, photoHeight);

        Calendar calendar = Calendar.getInstance();
        StringBuilder filename = new StringBuilder();
        filename.append(calendar.get(Calendar.YEAR)).append("/");
        filename.append(calendar.get(Calendar.MONTH) + 1).append("/");
        filename.append(calendar.get(Calendar.DAY_OF_MONTH)).append("/");
        filename.append(UUID.randomUUID().toString());
        filename.append(".jpg");

        // Create temp file
        File file = File.createTempFile("photo", ".jpg");

        if (inputStream != null && bufferedImageScaled.equals(bufferedImage)) {
            inputStream.reset();
            FileUtils.copyInputStreamToFile(inputStream, file);
        } else {
            Thumbnails.of(bufferedImageScaled).scale(1.0).outputQuality(1.0).toFile(file);
        }

        return storageProvider.store(file, relativePath, filename.toString());
    }

    protected static Integer getHeight(int width, BigDecimal ratio) {
        if (ratio != null) {
            return new BigDecimal(width).multiply(ratio).intValue();
        }

        return null;
    }

}
