package com.mb.lab.banks.user.business.dto.base;

public class SearchParamDto extends PageNumberRequestDto {

    private String search;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

}
