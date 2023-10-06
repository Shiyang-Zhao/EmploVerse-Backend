package com.java.springboot.EMSbackend.model.userModel;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9_-]{6,20}$")
	@Column(name = "username")
	private String username;

	@Email
	@NotBlank
	@Column(name = "email")
	private String email;

	@NotBlank
	@JsonIgnore
	@Size(min = 8)
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$")
	@Column(name = "password")
	private String password;

	@NotBlank
	@Pattern(regexp = "^(\\+?[0-9]*)?[0-9\\-\\s]{7,}$")
	@Column(name = "phone_number")
	private String phoneNumber;

	@NotBlank
	@Column(name = "profile_image")
	private String profileImage;

	@NotEmpty
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private List<Role> roles = new ArrayList<>();

	public User(String firstName, String lastName, String username, String email, String password,
			String phoneNumber, String profileImage, List<Role> roles) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.profileImage = profileImage;
		this.roles = roles;
	}
}