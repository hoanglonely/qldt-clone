package com.mb.lab.banks.auth.service.partner;

import com.mb.lab.banks.auth.service.base.CategoryDto;
import com.mb.lab.banks.utils.location.Location;

public class PartnerDto extends CategoryDto {

    private String slogan;
    private String logo;
    private String phone;
    private String email;
    private String website;
    private String description;
    private String address;
    private Location location;

    public String getSlogan() {
        return slogan;
    }

    public String getLogo() {
        return logo;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public Location getLocation() {
        return location;
    }

}
