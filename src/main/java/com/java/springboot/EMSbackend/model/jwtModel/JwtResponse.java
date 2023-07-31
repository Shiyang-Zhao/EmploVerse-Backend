package com.java.springboot.EMSbackend.model.jwtModel;

import java.io.Serializable;
import java.util.Collection;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final long id;
	private final String username;
	private final String jwttoken;
	private final Collection<String> roles;

	public JwtResponse(long id, String username, String jwttoken, Collection<String> roles) {
		this.id = id;
		this.username = username;
		this.jwttoken = jwttoken;
		this.roles = roles;
	}

	public long getId() {
		return this.id;
	}

	public String getUsername() {
		return this.username;
	}

	public String getToken() {
		return this.jwttoken;
	}

	public Collection<String> getRoles() {
		return this.roles;
	}
}