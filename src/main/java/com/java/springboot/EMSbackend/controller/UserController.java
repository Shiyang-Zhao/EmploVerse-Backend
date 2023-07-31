package com.java.springboot.EMSbackend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.springboot.EMSbackend.dto.UserDto;
import com.java.springboot.EMSbackend.model.jwtModel.JwtRequest;
import com.java.springboot.EMSbackend.model.jwtModel.JwtResponse;
import com.java.springboot.EMSbackend.model.userModel.User;
import com.java.springboot.EMSbackend.service.userService.JwtService;
import com.java.springboot.EMSbackend.service.userService.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = { "http://localhost:3000",
		"http://emploverse-frontend.herokuapp.com/" }, allowCredentials = "true")
@RequestMapping("/user")
public class UserController {

	private final UserService userService;
	private final JwtService jwtService;

	@Autowired
	public UserController(UserService userService, JwtService jwtService) {
		this.userService = userService;
		this.jwtService = jwtService;
	}

	// Admin or Manager rigister for employees and provide login info to employees
	@PostMapping("/register")
	// @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<?> register(@RequestBody UserDto userDto) throws Exception {
		return ResponseEntity.ok(jwtService.registeUser(userDto));
	}

	// User get login info from Admin or Manager to authenticate
	@PostMapping("/authenticate")
	// @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
	public ResponseEntity<?> authenticate(@RequestBody JwtRequest authenticationRequest) throws Exception {
		String token = jwtService.authenticateUser(authenticationRequest);
		long id = userService.getUserByUsername(authenticationRequest.getUsername()).getId();
		return ResponseEntity
				.ok(new JwtResponse(id, authenticationRequest.getUsername(), token, authenticationRequest.getRoles()));
	}

	// Only authenticated accounts can log out
	@PostMapping("/logout")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		jwtService.logoutUser(request);
		return ResponseEntity.ok("User is logged out successfully!");
	}

	// APIs for admin page
	// Get User APIs
	@GetMapping("/")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> usersList = userService.getAllUsers();
		return ResponseEntity.ok(usersList);
	}

	@GetMapping("/getUserById/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<User> getUserById(@PathVariable(value = "id") long id) {
		User user = userService.getUserById(id);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/getUserByUsername/{username}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<User> getUserByUsername(@PathVariable(value = "username") String username) {
		User user = userService.getUserByUsername(username);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/getUserByEmail/{email}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<User> getUserByEmail(@PathVariable(value = "email") String email) {
		User user = userService.getUserByEmail(email);
		return ResponseEntity.ok(user);
	}

	// Update user APIs
	@PostMapping("/updateUserById/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<String> updateUserById(@PathVariable(value = "id") long id,
			@RequestBody UserDto userDto) {
		userService.updateUserById(id, userDto);
		return ResponseEntity.ok("User is updated successfully!!!");
	}

	@PostMapping("/updateUserByUsername/{username}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<String> updateUserByUsername(@PathVariable(value = "username") String username,
			@RequestBody UserDto userDto) {
		userService.updateUserByUsername(username, userDto);
		return ResponseEntity.ok("User is updated successfully!!!");
	}

	@PostMapping("/updateUserByEmail/{email}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<String> updateUserByEmail(@PathVariable(value = "email") String email,
			@RequestBody UserDto userDto) {
		userService.updateUserByEmail(email, userDto);
		return ResponseEntity.ok("User is updated successfully!!!");
	}

	// Delete user APIs
	@PostMapping("/deleteUserById/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<String> deleteUserById(@PathVariable(value = "id") long id) {
		userService.deleteUserById(id);
		return ResponseEntity.ok("User is deleted successfully!!!");
	}

	@PostMapping("/deleteUserByUsername/{username}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<String> deleteUserByUsername(@PathVariable(value = "username") String username) {
		userService.deleteUserByUsername(username);
		return ResponseEntity.ok("User is deleted successfully!!!");
	}

	@PostMapping("/deleteUserByEmail/{email}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<String> deleteUserByEmail(@PathVariable(value = "email") String email) {
		userService.deleteUserByEmail(email);
		return ResponseEntity.ok("User is deleted successfully!!!");
	}

	@GetMapping("/page/{pageNo}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Map<String, Object>> getPaginatedUsers(@RequestBody List<User> users,
			@PathVariable(value = "pageNo") int pageNo,
			@RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
			@RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {
		int pageSize = 3;
		Page<User> page = userService.getPaginatedUsers(users, pageNo, pageSize, sortField, sortDir);

		Map<String, Object> response = new HashMap<>();
		response.put("currentPage", pageNo);
		response.put("totalPages", page.getTotalPages());
		response.put("totalItems", page.getTotalElements());
		response.put("sortField", sortField);
		response.put("sortDir", sortDir);
		response.put("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		response.put("listUsers", page.getContent());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/searchUsers")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
	public ResponseEntity<List<User>> searchUsers(
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "searchField", required = false, defaultValue = "id") String searchField) {
		List<User> searchResult = userService.searchUsers(keyword, searchField);
		return ResponseEntity.ok(searchResult);
	}

	@PostMapping("/getCurrentUser")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<User> getCurrentUser() {
		User user = userService.getCurrentUser();
		return ResponseEntity.ok(user);
	}

	@PostMapping("/updateCurrentUser")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<String> updateCurrentUser(@RequestBody UserDto userDto) {
		updateCurrentUser(userDto);
		return ResponseEntity.ok("Current user is updated successfully!!!");
	}
}
