package com.mb.lab.banks.utils.common;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Rotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;

import net.coobird.thumbnailator.Thumbnails;

public class ImageUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);

    private static final Pattern HEX_PATTERN = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");

    public static final BufferedImage fixExifOrientation(InputStream inputStream) throws IOException {
        int orientation = 1;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, baos);
        byte[] bytes = baos.toByteArray();

        try {
            Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(bytes));
            Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            if (directory != null) {
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }

            if (orientation >= 2 && orientation <= 8) {
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));

                switch (orientation) {
                    case 2:
                        return Scalr.rotate(bufferedImage, Rotation.FLIP_HORZ, Scalr.OP_ANTIALIAS);
                    case 3:
                        return Scalr.rotate(bufferedImage, Rotation.CW_180, Scalr.OP_ANTIALIAS);
                    case 4:
                        return Scalr.rotate(bufferedImage, Rotation.FLIP_VERT, Scalr.OP_ANTIALIAS);
                    case 5:
                        return Scalr.rotate(Scalr.rotate(bufferedImage, Rotation.FLIP_VERT, Scalr.OP_ANTIALIAS), Rotation.CW_90, Scalr.OP_ANTIALIAS);
                    case 6:
                        return Scalr.rotate(bufferedImage, Rotation.CW_90, Scalr.OP_ANTIALIAS);
                    case 7:
                        return Scalr.rotate(Scalr.rotate(bufferedImage, Rotation.FLIP_HORZ, Scalr.OP_ANTIALIAS), Rotation.CW_90, Scalr.OP_ANTIALIAS);
                    case 8:
                        return Scalr.rotate(bufferedImage, Rotation.CW_270, Scalr.OP_ANTIALIAS);
                    default:
                        break;
                }
            }
        } catch (MetadataException e) {
            LOGGER.error("Cannot read jpeg metadata", e);
        } catch (ImageProcessingException e) {
            LOGGER.error("Cannot read jpeg metadata", e);
        }

        return ImageIO.read(new ByteArrayInputStream(bytes));
    }

    public static final BufferedImage cropSquare(BufferedImage originalImage) {
        Assert.notNull(originalImage, "originalImage cannot be null");

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        if (width == height) {
            return originalImage;
        }

        int x;
        int y;
        int size;
        if (width > height) {
            x = (width - height) / 2;
            y = 0;
            size = height;
        } else {
            x = 0;
            y = (height - width) / 2;
            size = width;
        }

        return Scalr.crop(originalImage, x, y, size, size, Scalr.OP_ANTIALIAS);
    }

    public static final BufferedImage getCoverImage(BufferedImage originalImage, int width, Integer height, ImagePosition imagePosition) throws IOException {
        Assert.notNull(originalImage, "originalImage cannot be null");
        Assert.notNull(imagePosition, "imagePosition cannot be null");

        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        if (originalWidth == width && (height == null || originalHeight == height)) {
            return originalImage;
        }

        BigDecimal orignalRatio = new BigDecimal(originalWidth).divide(new BigDecimal(originalHeight), 5, RoundingMode.HALF_UP);

        BigDecimal newRatio = orignalRatio;
        if (height != null) {
            newRatio = new BigDecimal(width).divide(new BigDecimal(height), 5, RoundingMode.HALF_UP);
        } else {
            height = new BigDecimal(width).multiply(new BigDecimal(originalHeight)).divide(new BigDecimal(originalWidth), 0, RoundingMode.UP).intValue();
        }

        int ratioCompare = orignalRatio.compareTo(newRatio);
        BufferedImage imageResized = null;
        if (ratioCompare > 0) {
            int tmpWidth = new BigDecimal(height).multiply(new BigDecimal(originalWidth)).divide(new BigDecimal(originalHeight), 0, RoundingMode.UP).intValue();
            imageResized = ImageUtils.resizeImage(originalImage, tmpWidth, height);
        } else if (ratioCompare < 0) {
            int tmpHeight = new BigDecimal(width).multiply(new BigDecimal(originalHeight)).divide(new BigDecimal(originalWidth), 0, RoundingMode.UP).intValue();
            imageResized = ImageUtils.resizeImage(originalImage, width, tmpHeight);
        } else {
            return ImageUtils.resizeImage(originalImage, width, height);
        }

        int x = 0;
        int y = 0;
        switch (imagePosition) {
            case TOP_LEFT:
                break;
            case TOP_CENTER:
                x = new BigDecimal(imageResized.getWidth()).subtract(new BigDecimal(width)).divide(new BigDecimal(2), 0, RoundingMode.DOWN).intValue();
                break;
            case TOP_RIGHT:
                x = new BigDecimal(imageResized.getWidth()).subtract(new BigDecimal(width)).intValue();
                break;
            case MIDDLE_LEFT:
                y = new BigDecimal(imageResized.getHeight()).subtract(new BigDecimal(height)).divide(new BigDecimal(2), 0, RoundingMode.DOWN).intValue();
                break;
            case MIDDLE_CENTER:
                x = new BigDecimal(imageResized.getWidth()).subtract(new BigDecimal(width)).divide(new BigDecimal(2), 0, RoundingMode.DOWN).intValue();
                y = new BigDecimal(imageResized.getHeight()).subtract(new BigDecimal(height)).divide(new BigDecimal(2), 0, RoundingMode.DOWN).intValue();
                break;
            case MIDDLE_RIGHT:
                x = new BigDecimal(imageResized.getWidth()).subtract(new BigDecimal(width)).intValue();
                y = new BigDecimal(imageResized.getHeight()).subtract(new BigDecimal(height)).divide(new BigDecimal(2), 0, RoundingMode.DOWN).intValue();
                break;
            case BOTTOM_LEFT:
                y = new BigDecimal(imageResized.getHeight()).subtract(new BigDecimal(height)).intValue();
                break;
            case BOTTOM_CENTER:
                x = new BigDecimal(imageResized.getWidth()).subtract(new BigDecimal(width)).divide(new BigDecimal(2), 0, RoundingMode.DOWN).intValue();
                y = new BigDecimal(imageResized.getHeight()).subtract(new BigDecimal(height)).intValue();
                break;
            case BOTTOM_RIGHT:
                x = new BigDecimal(imageResized.getWidth()).subtract(new BigDecimal(width)).intValue();
                y = new BigDecimal(imageResized.getHeight()).subtract(new BigDecimal(height)).intValue();
                break;
            default:
                break;
        }

        return Scalr.crop(imageResized, x, y, width, height, Scalr.OP_ANTIALIAS);
    }

    public static final BufferedImage getContainImageWithMargin(BufferedImage originalImage,
            int width,
            int height,
            ImagePosition imagePosition,
            Color marginColor) throws IOException {
        Assert.notNull(originalImage, "originalImage cannot be null");
        Assert.notNull(imagePosition, "imagePosition cannot be null");
        Assert.notNull(marginColor, "marginColor cannot be null");

        BufferedImage imageResized = getContainImage(originalImage, width, height);

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(marginColor);
        g.fillRect(0, 0, width, height);

        int x = 0;
        int y = 0;
        switch (imagePosition) {
            case TOP_LEFT:
                break;
            case TOP_CENTER:
                x = new BigDecimal(width).subtract(new BigDecimal(imageResized.getWidth())).divide(new BigDecimal(2), 0, RoundingMode.DOWN).intValue();
                break;
            case TOP_RIGHT:
                x = new BigDecimal(width).subtract(new BigDecimal(imageResized.getWidth())).intValue();
                break;
            case MIDDLE_LEFT:
                y = new BigDecimal(height).subtract(new BigDecimal(imageResized.getHeight())).divide(new BigDecimal(2), 0, RoundingMode.DOWN).intValue();
                break;
            case MIDDLE_CENTER:
                x = new BigDecimal(width).subtract(new BigDecimal(imageResized.getWidth())).divide(new BigDecimal(2), 0, RoundingMode.DOWN).intValue();
                y = new BigDecimal(height).subtract(new BigDecimal(imageResized.getHeight())).divide(new BigDecimal(2), 0, RoundingMode.DOWN).intValue();
                break;
            case MIDDLE_RIGHT:
                x = new BigDecimal(width).subtract(new BigDecimal(imageResized.getWidth())).intValue();
                y = new BigDecimal(height).subtract(new BigDecimal(imageResized.getHeight())).divide(new BigDecimal(2), 0, RoundingMode.DOWN).intValue();
                break;
            case BOTTOM_LEFT:
                y = new BigDecimal(height).subtract(new BigDecimal(imageResized.getHeight())).intValue();
                break;
            case BOTTOM_CENTER:
                x = new BigDecimal(width).subtract(new BigDecimal(imageResized.getWidth())).divide(new BigDecimal(2), 0, RoundingMode.DOWN).intValue();
                y = new BigDecimal(height).subtract(new BigDecimal(imageResized.getHeight())).intValue();
                break;
            case BOTTOM_RIGHT:
                x = new BigDecimal(width).subtract(new BigDecimal(imageResized.getWidth())).intValue();
                y = new BigDecimal(height).subtract(new BigDecimal(imageResized.getHeight())).intValue();
                break;
            default:
                break;
        }

        g.drawImage(imageResized, x, y, null);

        return newImage;
    }

    public static final BufferedImage getContainImage(BufferedImage originalImage, int width, int height) throws IOException {
        Assert.notNull(originalImage, "originalImage cannot be null");

        originalImage = removeTransparent(originalImage);

        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        BigDecimal orignalRatio = new BigDecimal(originalWidth).divide(new BigDecimal(originalHeight), 5, RoundingMode.HALF_UP);
        BigDecimal newRatio = new BigDecimal(width).divide(new BigDecimal(height), 5, RoundingMode.HALF_UP);

        int ratioCompare = orignalRatio.compareTo(newRatio);
        BufferedImage imageResized = null;
        if (ratioCompare > 0) {
            int tmpHeight = new BigDecimal(width).multiply(new BigDecimal(originalHeight)).divide(new BigDecimal(originalWidth), 0, RoundingMode.DOWN)
                    .intValue();
            imageResized = ImageUtils.resizeImage(originalImage, width, tmpHeight);
        } else if (ratioCompare < 0) {
            int tmpWidth = new BigDecimal(height).multiply(new BigDecimal(originalWidth)).divide(new BigDecimal(originalHeight), 0, RoundingMode.DOWN)
                    .intValue();
            imageResized = ImageUtils.resizeImage(originalImage, tmpWidth, height);
        } else {
            if (originalWidth != width) {
                imageResized = ImageUtils.resizeImage(originalImage, width, height);
            } else {
                imageResized = originalImage;
            }
        }

        return imageResized;
    }

    public static final BufferedImage resizeImage(BufferedImage originalImage, int width, int height) throws IOException {
        Assert.notNull(originalImage, "originalImage cannot be null");

        if (width == originalImage.getWidth() && height == originalImage.getHeight()) {
            return originalImage;
        }

        // return Scalr.resize(originalImage, Method.AUTOMATIC, Mode.FIT_EXACT, width, height, Scalr.OP_ANTIALIAS);
        return Thumbnails.of(originalImage).forceSize(width, height).asBufferedImage();
    }

    public static final BufferedImage removeTransparent(BufferedImage originalImage) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        BufferedImage newImage = new BufferedImage(originalWidth, originalHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(originalImage, 0, 0, originalWidth, originalHeight, Color.WHITE, null);

        return newImage;
    }

    public static Color hex2Rgb(String colorStr) {
        return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    public static boolean idValidHexColor(String hex) {
        if (StringUtils.isEmpty(hex)) {
            return false;
        }

        Matcher matcher = HEX_PATTERN.matcher(hex);
        return matcher.matches();
    }

    public static enum ImagePosition {
        TOP_LEFT, TOP_CENTER, TOP_RIGHT, MIDDLE_LEFT, MIDDLE_CENTER, MIDDLE_RIGHT, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT,
    }

}
