package com.java.springboot.EMSbackend.model.userModel;

import java.io.Serializable;
import java.util.Collection;

import lombok.Data;

@Data
public class JwtResponse implements Serializable {

	// This needs to be initialized to be ignored by @Data
	private static final long serialVersionUID = -8091879091924046844L;

	private final String jwttoken;
	private final Collection<String> roles;
}