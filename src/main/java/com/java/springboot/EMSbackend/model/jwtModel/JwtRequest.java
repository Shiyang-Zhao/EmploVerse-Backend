package com.java.springboot.EMSbackend.model.jwtModel;

import java.io.Serializable;
import java.util.Collection;

public class JwtRequest implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;

	private String username;
	private String password;
	private Collection<String> roles;

	// default constructor for JSON Parsing
	public JwtRequest() {
	}

	public JwtRequest(String username, String password, Collection<String> roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public Collection<String> getRoles() {
		return roles;
	}
}