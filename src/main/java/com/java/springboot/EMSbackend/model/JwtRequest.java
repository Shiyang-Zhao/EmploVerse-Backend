package com.java.springboot.EMSbackend.model;

import java.io.Serializable;
import java.util.Collection;

public class JwtRequest implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;

	private String email;
	private String password;
	private Collection<String> roles;

	// default constructor for JSON Parsing
	public JwtRequest() {
	}

	public JwtRequest(String email, String password, Collection<String> roles) {
		this.setEmail(email);
		this.setPassword(password);
		this.setRoles(roles);
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<String> getRoles() {
		return roles;
	}

	public void setRoles(Collection<String> roles) {
		this.roles = roles;
	}
}