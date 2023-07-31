package com.java.springboot.EMSbackend.dto;

import java.util.List;

import com.java.springboot.EMSbackend.model.userModel.Role;

public class UserDto {
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String password;
	private String phoneNumber;
	private byte[] profileImage;
	private List<Role> roles;

	public UserDto() {

	}

	// Constructor
	public UserDto(String firstName, String lastName, String username, String email, String password,
			String phoneNumber, byte[] profileImage, List<Role> roles) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.profileImage = profileImage;
		this.roles = roles;
	}

	// Getters
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public byte[] getProfileImage() {
		return profileImage;
	}

	public List<Role> getRoles() {
		return roles;
	}

	// Setters
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setProfileImage(byte[] profileImage) {
		this.profileImage = profileImage;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}