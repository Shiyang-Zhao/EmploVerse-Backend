package com.java.springboot.EMSbackend.model;

import java.io.Serializable;
import java.util.Collection;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
	private final Collection<String> roles;

	public JwtResponse(String jwttoken, Collection<String> roles) {
		this.jwttoken = jwttoken;
		this.roles = roles;
	}

	public String getToken() {
		return this.jwttoken;
	}

	public Collection<String> getRoles() {
		return this.roles;
	}
}