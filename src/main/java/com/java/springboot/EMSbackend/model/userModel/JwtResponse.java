package com.java.springboot.EMSbackend.model.userModel;

import java.io.Serializable;
import java.util.Collection;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;

	private String jwt;
	private Collection<String> roles;

	public JwtResponse() {
	}

	public JwtResponse(String jwt, Collection<String> roles) {
		this.jwt = jwt;
		this.roles = roles;
	}

	public String getToken() {
		return this.jwt;
	}

	public Collection<String> getRoles() {
		return this.roles;
	}
}