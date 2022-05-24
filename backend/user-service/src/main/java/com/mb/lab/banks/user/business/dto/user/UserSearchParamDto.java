package com.mb.lab.banks.user.business.dto.user;

import java.util.Set;

import com.mb.lab.banks.user.business.dto.base.BaseSearchParamDto;

public class UserSearchParamDto extends BaseSearchParamDto {

    private String username;
    private String keyword;
    private Long storeId;
    private Long partnerId;
    private Set<Long> idList;
    private Set<String> usernameList;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public Set<Long> getIdList() {
        return idList;
    }

    public void setIdList(Set<Long> idList) {
        this.idList = idList;
    }

    public Set<String> getUsernameList() {
        return usernameList;
    }

    public void setUsernameList(Set<String> usernameList) {
        this.usernameList = usernameList;
    }
    
}
