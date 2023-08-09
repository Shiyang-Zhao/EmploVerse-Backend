package com.java.springboot.EMSbackend.service.userService;

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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.java.springboot.EMSbackend.dto.UserDto.UserDto;
import com.java.springboot.EMSbackend.model.userModel.User;
import com.java.springboot.EMSbackend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImplementation implements UserService, UserDetailsService {

	private final BCryptPasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	@Autowired
	public UserServiceImplementation(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
	}

	// Map roles to Authorities
	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found for Username: " + username));
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
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

	@Override
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found for Username: " + username));
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found for Email: " + email));
	}

	public void updateUser(User user, UserDto userDto) {
		try {
			user.setFirstName(userDto.getFirstName());
			user.setLastName(userDto.getLastName());
			user.setUsername(userDto.getUsername());
			user.setEmail((userDto.getEmail()));
			user.setPassword(passwordEncoder.encode(userDto.getPassword()));
			user.setPhoneNumber(userDto.getPhoneNumber());
			user.setRoles(userDto.getRoles());
			userRepository.save(user);
		} catch (Exception e) {
			throw new RuntimeException("Failed to update user: " + e.getMessage());
		}
	}

	@Override
	public void updateUserById(long id, UserDto userDto) {
		User user = getUserById(id);
		updateUser(user, userDto);
	}

	@Override
	public void updateUserByUsername(String username, UserDto userDto) {
		User user = getUserByUsername(username);
		updateUser(user, userDto);
	}

	@Override
	public void updateUserByEmail(String email, UserDto userDto) {
		User user = getUserByEmail(email);
		updateUser(user, userDto);
	}

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
	@Transactional
	public void deleteUserByUsername(String username) {
		try {
			userRepository.deleteByUsername(username);
		} catch (Exception e) {
			// Handle any exceptions thrown during user deletion
			throw new RuntimeException("Failed to delete user by Username: " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public void deleteUserByEmail(String email) {
		try {
			userRepository.deleteByEmail(email);
		} catch (Exception e) {
			// Handle any exceptions thrown during user deletion
			throw new RuntimeException("Failed to delete user by Email: " + e.getMessage());
		}
	}

	@Override
	public Page<User> getPaginatedUsers(List<User> userList, int pageNo, int pageSize, String sortField,
			String sortDir) {
		try {
			Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
					: Sort.by(sortField).descending();

			Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
			int startIndex = (pageNo - 1) * pageSize;
			int endIndex = Math.min(startIndex + pageable.getPageSize(), userList.size());
			List<User> paginatedList = userList.subList(startIndex, endIndex);
			return new PageImpl<>(paginatedList, pageable, userList.size());
		} catch (Exception e) {
			// Handle any exceptions thrown during paginated user retrieval
			throw new RuntimeException("Failed to retrieve paginated users: " + e.getMessage());
		}
	}

	private Function<User, String> createFieldToGetterMap(String searchField) {
		Map<String, Function<User, String>> fieldToGetterMap = new HashMap<>();
		fieldToGetterMap.put("id", user -> String.valueOf(user.getId()));
		fieldToGetterMap.put("firstName", user -> user.getFirstName());
		fieldToGetterMap.put("lastName", user -> user.getLastName());
		fieldToGetterMap.put("username", user -> user.getUsername());
		fieldToGetterMap.put("email", user -> user.getEmail());
		fieldToGetterMap.put("phoneNumber", user -> user.getPhoneNumber());

		return fieldToGetterMap.get(searchField);
	}

	@Override
	public List<User> searchUsers(String keyword, String searchField) {
		try {
			List<User> allUsersList = getAllUsers();
			String lowercaseKeyword = keyword.toLowerCase(); // Convert the keyword to lowercase

			if (keyword.isEmpty()) {
				// If the search keyword is empty, return all users using findPaginated
				// method
				return getAllUsers();
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

	// @Override
	// public List<User> sortUsers(List<User> users, String sortField, String
	// sortDir) {
	// try {
	// Function<User, String> getter = createFieldToGetterMap(sortField);
	// if (getter == null) {
	// throw new IllegalArgumentException("Invalid sortField: " + sortField);
	// }

	// Comparator<User> comparator = Comparator.comparing(getter);
	// if (sortDir.equalsIgnoreCase("desc")) {
	// comparator = comparator.reversed();
	// }

	// return users.stream()
	// .sorted(comparator)
	// .collect(Collectors.toList());
	// } catch (Exception e) {
	// throw new RuntimeException("Failed to sort users: " + e.getMessage());
	// }
	// }

	@Override
	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Failed to get details of the logged-in user"));
	}

	@Override
	public void updateCurrentUser(UserDto userDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Failed to update details of the logged-in user"));
		updateUser(user, userDto);
	}

}
