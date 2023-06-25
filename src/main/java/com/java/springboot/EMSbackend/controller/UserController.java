package com.java.springboot.EMSbackend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.springboot.EMSbackend.dto.UserDto;
import com.java.springboot.EMSbackend.model.JwtRequest;
import com.java.springboot.EMSbackend.model.JwtResponse;
import com.java.springboot.EMSbackend.model.User;
import com.java.springboot.EMSbackend.service.JwtService;
import com.java.springboot.EMSbackend.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = { "http://localhost:3000",
		"http://emploverse-frontend.herokuapp.com/" }, allowCredentials = "true")
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtService jwtService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserDto userDto) throws Exception {
		return ResponseEntity.ok(jwtService.registeUser(userDto));
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody JwtRequest authenticationRequest) throws Exception {
		String token = jwtService.authenticateUser(authenticationRequest);
		return ResponseEntity.ok(new JwtResponse(token, authenticationRequest.getRoles()));
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		jwtService.logoutUser(request);
		return ResponseEntity.ok("User is logged out successfully!");
	}

	@GetMapping("/getUserById/{id}")
	public ResponseEntity<User> getUserById(@PathVariable(value = "id") long id) {
		User user = userService.getUserById(id);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/getUserByEmail/{email}")
	public ResponseEntity<User> getUserByEmail(@PathVariable(value = "email") String email) {
		User user = userService.getUserByEmail(email);
		return ResponseEntity.ok(user);
	}

	@PostMapping("/updateUserById/{id}")
	public ResponseEntity<String> updateUserById(@PathVariable(value = "id") long id,
			@RequestBody UserDto userDto) {
		userService.updateUserById(id, userDto);
		return ResponseEntity.ok("User is updated successfully!!!");
	}

	@PostMapping("/updateUserByEmail/{email}")
	public ResponseEntity<String> updateUserByEmail(@PathVariable(value = "email") String email,
			@RequestBody UserDto userDto) {
		userService.updateUserByEmail(email, userDto);
		return ResponseEntity.ok("User is updated successfully!!!");
	}

	@PostMapping("/deleteUserById/{id}")
	public ResponseEntity<String> deleteUserById(@PathVariable(value = "id") long id) {
		userService.deleteUserById(id);
		return ResponseEntity.ok("User is deleted successfully!!!");
	}

	@PostMapping("/deleteUserByEmail/{email}")
	public ResponseEntity<String> deleteUserByEmail(@PathVariable(value = "email") String email) {
		userService.deleteUserByEmail(email);
		return ResponseEntity.ok("User is deleted successfully!!!");
	}

	@GetMapping("/page/{pageNo}")
	public ResponseEntity<Map<String, Object>> findPaginated(@PathVariable(value = "pageNo") int pageNo,
			@RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
			@RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {
		int pageSize = 3;
		Page<User> page = userService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<User> listUsers = page.getContent();

		Map<String, Object> response = new HashMap<>();
		response.put("currentPage", pageNo);
		response.put("totalPages", page.getTotalPages());
		response.put("totalItems", page.getTotalElements());
		response.put("sortField", sortField);
		response.put("sortDir", sortDir);
		response.put("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		response.put("listUsers", listUsers);

		return ResponseEntity.ok(response);
	}
}
