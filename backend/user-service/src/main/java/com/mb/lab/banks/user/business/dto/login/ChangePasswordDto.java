package com.mb.lab.banks.user.business.dto.login;

import java.io.Serializable;

public class ChangePasswordDto implements Serializable {

    private static final long serialVersionUID = 7767997102832585572L;

    private String oldPassword;
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
