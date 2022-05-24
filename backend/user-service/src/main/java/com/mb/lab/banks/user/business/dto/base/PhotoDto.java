package com.mb.lab.banks.user.business.dto.base;

import java.io.Serializable;

import com.mb.lab.banks.user.entity.PhotoDimension;

public class PhotoDto implements Serializable {

    private static final long serialVersionUID = -7878469787971989668L;

    public static final PhotoDto EMPTY = new PhotoDto(null, new PhotoDimension());

    private String url;
    private PhotoDimension dimension;

    public PhotoDto(String url, PhotoDimension dimension) {
        super();

        this.url = url;
        this.dimension = dimension;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PhotoDimension getDimension() {
        return dimension;
    }

    public void setDimension(PhotoDimension dimension) {
        this.dimension = dimension;
    }

}
