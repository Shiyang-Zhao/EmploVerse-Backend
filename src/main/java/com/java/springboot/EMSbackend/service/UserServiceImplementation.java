package com.java.springboot.EMSbackend.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.java.springboot.EMSbackend.dto.UserDto;
import com.java.springboot.EMSbackend.model.Role;
import com.java.springboot.EMSbackend.model.User;
import com.java.springboot.EMSbackend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImplementation implements UserService, UserDetailsService {

	private UserRepository userRepository;

	@Autowired
	public UserServiceImplementation(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found for ID: " + email));
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
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
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found for ID: " + email));
	}

	public void updateUser(User user, UserDto userDto) {
		try {
			user.setFirstName(userDto.getFirstName());
			user.setLastName(userDto.getLastName());
			user.setEmail(userDto.getEmail());
			user.setBirthday(userDto.getBirthday());
			user.setPhoneNumber(userDto.getPhoneNumber());
			user.setAddress1(userDto.getAddress1());
			user.setAddress2(userDto.getAddress2());
			user.setCity(userDto.getCity());
			user.setState(userDto.getState());
			user.setZipCode(userDto.getZipCode());
			user.setCountry(userDto.getCountry());
			user.setCompany(userDto.getCompany());
			user.setStartDate(userDto.getStartDate());
			user.setEndDate(userDto.getEndDate());
			user.setDepartment(userDto.getDepartment());
			user.setSupervisor(userDto.getSupervisor());
			user.setJobTitles(userDto.getJobTitles());
			user.setWorkSchedule(userDto.getWorkSchedule());
			user.setStatus(userDto.getStatus());
			user.setUniversity(userDto.getUniversity());
			user.setDegree(userDto.getDegree());
			user.setMajor(userDto.getMajor());
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
			throw new RuntimeException("Failed to delete user: " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public void deleteUserByEmail(String email) {
		try {
			userRepository.deleteByEmail(email);
		} catch (Exception e) {
			// Handle any exceptions thrown during user deletion
			throw new RuntimeException("Failed to delete user: " + e.getMessage());
		}
	}

	@Override
	public Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		try {
			Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
					: Sort.by(sortField).descending();

			Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
			return userRepository.findAll(pageable);
		} catch (Exception e) {
			// Handle any exceptions thrown during paginated user retrieval
			throw new RuntimeException("Failed to retrieve paginated users: " + e.getMessage());
		}
	}

}
