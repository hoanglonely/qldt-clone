package com.mb.lab.banks.user.util.persistence;

import org.springframework.util.Assert;

public class PageNumberRequest extends PageRequest {

    private static final long serialVersionUID = -8547678954087532240L;

    public PageNumberRequest(int pageSize, int pageNumber) {
        super();

        Assert.isTrue(pageSize > 0, "page size must be > 0");
        Assert.isTrue(pageNumber >= 0, "page number must be >= 0");

        this.setPageSize(pageSize);
        this.setPageNumber(pageNumber);
        super.setMinId(null);
        super.setMaxId(null);
    }

    @Override
    public void setMinId(Long minId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxId(Long maxId) {
        throw new UnsupportedOperationException();
    }

}
