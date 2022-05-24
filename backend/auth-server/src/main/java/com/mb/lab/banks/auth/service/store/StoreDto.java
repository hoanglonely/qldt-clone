package com.mb.lab.banks.auth.service.store;

import com.mb.lab.banks.auth.service.base.CategoryDto;
import com.mb.lab.banks.auth.service.base.ReviewStatus;
import com.mb.lab.banks.utils.location.Location;

public class StoreDto extends CategoryDto {

    private String address;
    private String phone;
    private Location location;
    private CategoryDto province;
    private ReviewStatus status;
    private String reason;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public CategoryDto getProvince() {
        return province;
    }

    public void setProvince(CategoryDto province) {
        this.province = province;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
