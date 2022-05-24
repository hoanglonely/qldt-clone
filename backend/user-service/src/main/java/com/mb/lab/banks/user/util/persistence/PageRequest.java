package com.mb.lab.banks.user.util.persistence;

import java.io.Serializable;

public abstract class PageRequest implements Serializable {

    private static final long serialVersionUID = 6149378937361479918L;

    private int pageSize;
    private Integer pageNumber;
    private Long minId;
    private Long maxId;

    protected PageRequest() {
        super();
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Long getMinId() {
        return minId;
    }

    public void setMinId(Long minId) {
        this.minId = minId;
    }

    public Long getMaxId() {
        return maxId;
    }

    public void setMaxId(Long maxId) {
        this.maxId = maxId;
    }

}
