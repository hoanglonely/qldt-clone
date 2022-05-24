package com.mb.lab.banks.utils.security;

import java.io.Serializable;

public class UserLogin implements Serializable {

	private static final long serialVersionUID = 1;

	public static final String ADMIN = "ADMIN";
	public static final String SUB_ADMIN = "SUB_ADMIN";

	private String role;
	private Long id;
	private String username;

	public UserLogin() {
		super();
	}

	public UserLogin(String role, Long id, String username) {
		super();

		this.role = role;
		this.id = id;
		this.username = username;
	}

	public boolean isRole(String... roles) {
		if (this.role != null && roles != null) {
			for (String role : roles) {
				if (this.role.equals(role)) {
					return true;
				}
			}
		}

		return false;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
