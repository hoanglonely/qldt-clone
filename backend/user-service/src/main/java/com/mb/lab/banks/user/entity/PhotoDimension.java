package com.mb.lab.banks.user.entity;

import java.io.Serializable;

public class PhotoDimension implements Serializable {

    private static final long serialVersionUID = -876519388817943259L;

    private int width;
    private int height;

    public PhotoDimension() {
        this(0, 0);
    }

    public PhotoDimension(int width, int height) {
        super();
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
