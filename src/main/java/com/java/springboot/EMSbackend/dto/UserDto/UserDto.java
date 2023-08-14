package com.java.springboot.EMSbackend.dto.UserDto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.java.springboot.EMSbackend.model.userModel.Role;

public class UserDto {
	private long id;
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String password1;
	private String password2;
	private String phoneNumber;
	private MultipartFile profileImage;
	private List<Role> roles;

	public UserDto() {

	}

	// Constructor
	public UserDto(String firstName, String lastName, String username, String email, String password1, String password2,
			String phoneNumber, MultipartFile profileImage, List<Role> roles) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password1 = password1;
		this.password2 = password2;
		this.phoneNumber = phoneNumber;
		this.profileImage = profileImage;
		this.roles = roles;
	}

	public UserDto(long id, String firstName, String lastName, String username, String email, String password1,
			String password2,
			String phoneNumber, MultipartFile profileImage, List<Role> roles) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password1 = password1;
		this.password2 = password2;
		this.phoneNumber = phoneNumber;
		this.profileImage = profileImage;
		this.roles = roles;
	}

	// Getters
	public long getId() {
		return id;
	}

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

	public String getPassword1() {
		return password1;
	}

	public String getPassword2() {
		return password2;
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
	public void setId(long id) {
		this.id = id;
	}

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

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
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