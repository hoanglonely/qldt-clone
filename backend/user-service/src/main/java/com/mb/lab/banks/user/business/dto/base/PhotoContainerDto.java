package com.mb.lab.banks.user.business.dto.base;

import java.io.Serializable;

public class PhotoContainerDto implements Serializable {

    private static final long serialVersionUID = -207177405130230522L;

    public static final PhotoContainerDto EMPTY = new PhotoContainerDto(null, PhotoDto.EMPTY, PhotoDto.EMPTY, PhotoDto.EMPTY);

    private String key;
    private PhotoDto small;
    private PhotoDto medium;
    private PhotoDto large;

    public PhotoContainerDto(String key, PhotoDto small, PhotoDto medium, PhotoDto large) {
        super();

        this.key = key;
        this.small = small;
        this.medium = medium;
        this.large = large;
    }

    public String getKey() {
        return key;
    }

    public PhotoDto getSmall() {
        return small;
    }

    public PhotoDto getMedium() {
        return medium;
    }

    public PhotoDto getLarge() {
        return large;
    }

}
