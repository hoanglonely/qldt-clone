package com.mb.lab.banks.user.business.dto.base;

import java.io.Serializable;

public class PageNextRequestDto implements Serializable {

    private static final long serialVersionUID = 2200584815305026560L;

    private Integer pageSize;
    private Long minId;
    private Long maxId;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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
