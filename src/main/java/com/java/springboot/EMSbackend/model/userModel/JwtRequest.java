package com.java.springboot.EMSbackend.model.userModel;

import java.io.Serializable;
import java.util.Collection;

import lombok.Data;

@Data
public class JwtRequest implements Serializable {

	//This needs to be initialized to be ignored by @Data
	private static final long serialVersionUID = 5926468583005150707L;

	private String usernameOrEmail;
	private String password;
	private Collection<String> roles;

}