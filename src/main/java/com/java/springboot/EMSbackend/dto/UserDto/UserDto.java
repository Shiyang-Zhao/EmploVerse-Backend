package com.java.springboot.EMSbackend.dto.UserDto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.java.springboot.EMSbackend.model.userModel.Role;

public class UserDto {
	// private long id;
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String password;
	private String phoneNumber;
	private MultipartFile profileImage;
	private List<Role> roles;

	public UserDto() {

	}

	// Constructor
	public UserDto(String firstName, String lastName, String username, String email, String password,
			String phoneNumber, MultipartFile profileImage, List<Role> roles) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.profileImage = profileImage;
		this.roles = roles;
	}

	// public UserDto(long id, String firstName, String lastName, String username, String email, String password,
	// 		String phoneNumber, MultipartFile profileImage, List<Role> roles) {
	// 	this.id = id;
	// 	this.firstName = firstName;
	// 	this.lastName = lastName;
	// 	this.username = username;
	// 	this.email = email;
	// 	this.password = password;
	// 	this.phoneNumber = phoneNumber;
	// 	this.profileImage = profileImage;
	// 	this.roles = roles;
	// }

	// Getters
	// public long getId() {
	// 	return id;
	// }

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

	public MultipartFile getProfileImage() {
		return profileImage;
	}

	public List<Role> getRoles() {
		return roles;
	}

	// Setters
	// public void setId(long id) {
	// 	this.id = id;
	// }

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

	public void setProfileImage(MultipartFile profileImage) {
		this.profileImage = profileImage;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}