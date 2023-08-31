package com.java.springboot.EMSbackend.dto.UserDto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.java.springboot.EMSbackend.model.userModel.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@NotBlank
	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9_-]{6,20}$")
	private String username;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	@Size(min = 8)
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$")
	private String password1;

	@NotBlank
	@Size(min = 8)
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$")
	private String password2;

	@NotBlank
	@Pattern(regexp = "^(\\+?[0-9]*)?[0-9\\-\\s]{7,}$")
	private String phoneNumber;

	@NotNull
	private MultipartFile profileImage;

	@NotEmpty
	private List<Role> roles;
}