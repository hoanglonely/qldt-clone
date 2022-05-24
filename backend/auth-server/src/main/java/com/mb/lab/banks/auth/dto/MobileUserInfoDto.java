package com.mb.lab.banks.auth.dto;

import java.util.List;

import com.mb.lab.banks.auth.service.store.StoreDto;

public class MobileUserInfoDto {

    private Long userId;
    private String account;
    private int userType;
    private String avatar;
    private List<MobileShopInfoDto> listShop;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<MobileShopInfoDto> getListShop() {
        return listShop;
    }

    public void setListShop(List<MobileShopInfoDto> listShop) {
        this.listShop = listShop;
    }

    public static class MobileShopInfoDto {

        private Long shopId;
        private String shopCode;
        private String shopName;
        
        public MobileShopInfoDto(StoreDto store) {
            this.shopId = store.getId();
            this.shopCode = store.getCode();
            this.shopName = store.getName();
        }

        public Long getShopId() {
            return shopId;
        }

        public void setShopId(Long shopId) {
            this.shopId = shopId;
        }

        public String getShopCode() {
            return shopCode;
        }

        public void setShopCode(String shopCode) {
            this.shopCode = shopCode;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

    }
}
