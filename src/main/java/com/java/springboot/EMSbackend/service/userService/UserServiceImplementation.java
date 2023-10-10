package com.java.springboot.EMSbackend.service.userService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.java.springboot.EMSbackend.dto.UserDto.UserDto;
import com.java.springboot.EMSbackend.model.employeeModel.Employee;
import com.java.springboot.EMSbackend.model.userModel.User;
import com.java.springboot.EMSbackend.repository.EmployeeRepository;
import com.java.springboot.EMSbackend.repository.UserRepository;
import com.java.springboot.EMSbackend.service.S3Service.S3Service;

@Service
public class UserServiceImplementation implements UserService, UserDetailsService {

	private final BCryptPasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	private final EmployeeRepository employeeRepository;

	private final S3Service s3Service;

	@Autowired
	public UserServiceImplementation(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository,
			EmployeeRepository employeeRepository, S3Service s3Service) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.employeeRepository = employeeRepository;
		this.s3Service = s3Service;
	}

	// Map roles to Authorities
	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) {
		User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(() -> new UsernameNotFoundException(
						"User does not exist with username or email: " + usernameOrEmail));
		return new org.springframework.security.core.userdetails.User(usernameOrEmail, user.getPassword(),
				user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName()))
						.collect(Collectors.toList()));
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User getUserById(long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found for ID: " + id));
	}

	// @Override
	// public User getUserByUsername(String username) {
	// return userRepository.findByUsername(username)
	// .orElseThrow(() -> new RuntimeException("User not found for Username: " +
	// username));
	// }

	// @Override
	// public User getUserByEmail(String email) {
	// return userRepository.findByEmail(email)
	// .orElseThrow(() -> new RuntimeException("User not found for Email: " +
	// email));
	// }

	@Override
	public User getUserByUsernameOrEmail(String usernameOrEmail) {
		return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(
						() -> new RuntimeException("User does not exist with username or email: " + usernameOrEmail));
	}

	private void updateUserWithoutPasswordAndProfileImage(User user, UserDto userDto) {
		try {
			user.setFirstName(userDto.getFirstName());
			user.setLastName(userDto.getLastName());
			user.setUsername(userDto.getUsername());
			user.setEmail(userDto.getEmail());
			// user.setPassword(passwordEncoder.encode(userDto.getPassword()));
			user.setPhoneNumber(userDto.getPhoneNumber());
			// if (userDto.getPassword1() == userDto.getPassword2()) {
			// user.setPassword(userDto.getPassword1());
			// userRepository.save(user);
			// } else {
			// throw new IllegalArgumentException("Passwords do not match");
			// }
			userRepository.save(user);
		} catch (Exception e) {
			throw new RuntimeException("Failed to update user: " + e.getMessage());
		}
	}

	@Override
	public void updateUserById(long id, UserDto userDto) {
		User user = getUserById(id);
		updateUserWithoutPasswordAndProfileImage(user, userDto);
	}

	// @Override
	// public void updateUserByUsername(String username, UserDto userDto) {
	// User user = getUserByUsername(username);
	// updateUserWithoutPasswordAndProfileImage(user, userDto);
	// }

	// @Override
	// public void updateUserByEmail(String email, UserDto userDto) {
	// User user = getUserByEmail(email);
	// updateUserWithoutPasswordAndProfileImage(user, userDto);
	// }

	@Override
	public void deleteUserById(long id) {
		try {
			userRepository.deleteById(id);
		} catch (Exception e) {
			// Handle any exceptions thrown during user deletion
			throw new RuntimeException("Failed to delete user by ID: " + e.getMessage());
		}
	}

	@Override
	public void deleteUserByUsername(String username) {
		try {
			userRepository.deleteByUsername(username);
		} catch (Exception e) {
			// Handle any exceptions thrown during user deletion
			throw new RuntimeException("Failed to delete user by Username: " + e.getMessage());
		}
	}

	@Override
	public void deleteUserByEmail(String email) {
		try {
			userRepository.deleteByEmail(email);
		} catch (Exception e) {
			// Handle any exceptions thrown during user deletion
			throw new RuntimeException("Failed to delete user by Email: " + e.getMessage());
		}
	}

	private Function<User, String> createFieldToGetterMap(String field) {
		Map<String, Function<User, String>> fieldToGetterMap = new HashMap<>();
		fieldToGetterMap.put("id", user -> String.valueOf(user.getId()));
		fieldToGetterMap.put("firstName", user -> user.getFirstName());
		fieldToGetterMap.put("lastName", user -> user.getLastName());
		fieldToGetterMap.put("username", user -> user.getUsername());
		fieldToGetterMap.put("email", user -> user.getEmail());
		fieldToGetterMap.put("phoneNumber", user -> user.getPhoneNumber());

		return fieldToGetterMap.get(field);
	}

	@Override
	public Page<User> getPaginatedUsers(int pageNo, int pageSize, String sortField,
			String sortDir) {
		try {
			List<User> allUsers = getAllUsers();

			if (sortField != null && !sortField.isEmpty()) {
				Function<User, String> getter = createFieldToGetterMap(sortField);

				if (getter != null) {
					Comparator<User> comparator;
					if ("id".equals(sortField)) {
						comparator = Comparator.comparingLong(user -> user.getId());
					} else {
						comparator = Comparator.comparing(getter);
					}

					if ("desc".equalsIgnoreCase(sortDir)) {
						comparator = comparator.reversed();
					}
					allUsers.sort(comparator);
				}
			}
			int fromIndex = (pageNo - 1) * pageSize;
			int toIndex = Math.min(fromIndex + pageSize, allUsers.size());
			List<User> paginatedUsers = allUsers.subList(fromIndex, toIndex);
			return new PageImpl<>(paginatedUsers, PageRequest.of(pageNo - 1, pageSize), allUsers.size());
		} catch (Exception e) {
			throw new RuntimeException("Failed to retrieve paginated users: " + e.getMessage());
		}
	}

	@Override
	public List<User> searchUsers(String keyword, String searchField) {
		try {
			List<User> allUsersList = getAllUsers();
			String lowercaseKeyword = keyword.toLowerCase(); // Convert the keyword to lowercase

			if (keyword.isEmpty()) {
				return new ArrayList<>();
			}
			Function<User, String> getter = createFieldToGetterMap(searchField);
			if (getter == null) {
				throw new IllegalArgumentException("Invalid searchField: " + searchField);
			}
			// Define a predicate to filter users based on the keyword and search field
			Predicate<User> searchPredicate = user -> getter.apply(user).toLowerCase()
					.contains(lowercaseKeyword);

			// Apply the search predicate to filter the users
			List<User> searchResult = allUsersList.stream().filter(searchPredicate)
					.collect(Collectors.toList());

			return searchResult;
		} catch (Exception e) {
			throw new RuntimeException("Failed to search user: " + e.getMessage());
		}
	}

	@Override
	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String usernameOrEmail = authentication.getName();
		return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(() -> new RuntimeException("Failed to get details of the logged-in user"));
	}

	@Override
	public void updateCurrentUser(UserDto userDto) {
		User user = getCurrentUser();
		updateUserWithoutPasswordAndProfileImage(user, userDto);
	}

	@Override
	public void addCurrentUserToEmployees() {
		User user = getCurrentUser();
		Employee employee = new Employee(user);
		employeeRepository.save(employee);
	}

	@Override
	public void updateCurrentUserPassword(String newPassword) {
		User user = getCurrentUser();
		user.setPassword(passwordEncoder.encode(newPassword));
	}

	@Override
	public void updateCurrentUserProfileIamge(MultipartFile newProfileImage) {
		try {
			User user = getCurrentUser();
			if (newProfileImage != null && !newProfileImage.isEmpty()) {
				// Use the previously defined method to handle the S3 upload
				UserDto userDto = new UserDto();
				userDto.setUsername(user.getUsername());
				userDto.setProfileImage(newProfileImage);
				final String s3Path = s3Service.getPreUploadS3Path(userDto);
				String newProfileImagePath = s3Service.uploadProfileImageToS3(userDto, s3Path);

				// Update the user's profile image in the database
				user.setProfileImage(newProfileImagePath);
				userRepository.save(user);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to update the profile image: " + e.getMessage());
		}
	}

	@Override
	public Employee getCurrentEmployee() {
		User user = getCurrentUser();
		System.out.print(employeeRepository.findByUser(user));
		return employeeRepository.findByUser(user)
				.orElseThrow(() -> new RuntimeException("Current user does not have a corrsponding employee"));
	}

}
