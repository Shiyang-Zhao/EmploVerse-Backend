package com.java.springboot.EMSbackend.model.userModel;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email"),
		@UniqueConstraint(columnNames = "phone_number") }, indexes = {
				@Index(name = "idx_user_username", columnList = "username"),
				@Index(name = "idx_user_email", columnList = "email"),
				@Index(name = "idx_user_phoneNumber", columnList = "phone_number")
		})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private long id;

	// Account information
	@NotBlank
	@Column(name = "first_name")
	private String firstName;

	@NotBlank
	@Column(name = "last_name")
	private String lastName;

	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9]{5,15}$")
	@Column(name = "username")
	private String username;

	@NotBlank
	@Email
	@Column(name = "email")
	private String email;

	@NotBlank
	@Size(min = 8)
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$")
	@Column(name = "password")
	private String password;

	@NotBlank
	@Pattern(regexp = "^(\\+?[0-9]*)?[0-9\\-\\s]{7,}$")
	@Column(name = "phone_number")
	private String phoneNumber;

	@Lob
	@NotNull
	@Size(max = 5242880)
	@Pattern(regexp = "(image/jpeg|image/png)")
	@Column(name = "profile_image")
	private byte[] profileImage;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@NotEmpty
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private List<Role> roles = new ArrayList<>();

	// @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	// @NotNull
	// private Employee employee;

	// Constructors
	public User() {
	}

	public User(String firstName, String lastName, String username, String email, String password,
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

	// Getters and Setters
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