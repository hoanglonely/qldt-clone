package com.mb.lab.banks.user.business.dto.base;

import com.mb.lab.banks.user.persistence.domain.entity.ActiveStatus;

public class BaseSearchParamDto extends PageNumberRequestDto {

    private ActiveStatus activeStatus;
    private String search;

    public ActiveStatus getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(ActiveStatus activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

}
