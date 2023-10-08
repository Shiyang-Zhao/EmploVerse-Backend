package com.java.springboot.EMSbackend.model.userModel;

import java.io.Serializable;
import java.util.Collection;

public class JwtRequest implements Serializable {

	// This needs to be initialized to be ignored by @Data
	private static final long serialVersionUID = 5926468583005150707L;

	private String usernameOrEmail;
	private String password;
	private Collection<String> selectedRoles;

	// default constructor for JSON Parsing
	public JwtRequest() {
	}

	public JwtRequest(String usernameOrEmail, String password, Collection<String> selectedRoles) {
		this.usernameOrEmail = usernameOrEmail;
		this.password = password;
		this.selectedRoles = selectedRoles;
	}

	public String getUsernameOrEmail() {
		return this.usernameOrEmail;
	}

	public String getPassword() {
		return this.password;
	}

	public Collection<String> getSelectedRoles() {
		return this.selectedRoles;
	}
}