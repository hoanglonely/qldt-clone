package com.mb.lab.banks.auth.service.user.model;

public class UserLoginDto {

    private Long id;
    private String role;
    private String fullname;
    private String username;
    private String password;
    private boolean active;

	public Long getId() {
		return id;
	}

    public String getRole() {
        return role;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }

}

