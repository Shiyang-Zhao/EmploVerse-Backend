package com.java.springboot.EMSbackend.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.java.springboot.EMSbackend.dto.EmployeeDto;
import com.java.springboot.EMSbackend.model.Employee;
import com.java.springboot.EMSbackend.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

@Service
public class EmployeeServiceImplementation implements EmployeeService {

	private EmployeeRepository employeeRepository;

	@Autowired
	public EmployeeServiceImplementation(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public void saveEmployee(EmployeeDto employeeDto) {
		try {
			Employee newEmployee = new Employee(employeeDto.getFirstName(), employeeDto.getLastName(),
					employeeDto.getEmail(), employeeDto.getBirthday(),
					employeeDto.getPhoneNumber(), employeeDto.getAddress1(), employeeDto.getAddress2(),
					employeeDto.getCity(), employeeDto.getState(),
					employeeDto.getZipCode(), employeeDto.getCountry(), employeeDto.getCompany(),
					employeeDto.getStartDate(), employeeDto.getEndDate(), employeeDto.getDepartment(),
					employeeDto.getSupervisor(),
					employeeDto.getJobTitles(), employeeDto.getWorkSchedule(), employeeDto.getStatus(),
					employeeDto.getUniversity(), employeeDto.getDegree(),
					employeeDto.getMajor());
			employeeRepository.save(newEmployee);
		} catch (Exception e) {
			throw new RuntimeException("Failed to save employee: " + e.getMessage());
		}
	}

	@Override
	public Employee getEmployeeById(long id) {
		return employeeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Employee not found for ID: " + id));
	}

	@Override
	public Employee getEmployeeByEmail(String email) {
		return employeeRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Employee not found for email: " + email));
	}

	public void updateEmployee(Employee employee, EmployeeDto employeeDto) {
		try {
			employee.setFirstName(employeeDto.getFirstName());
			employee.setLastName(employeeDto.getLastName());
			employee.setEmail(employeeDto.getEmail());
			employee.setBirthday(employeeDto.getBirthday());
			employee.setPhoneNumber(employeeDto.getPhoneNumber());
			employee.setAddress1(employeeDto.getAddress1());
			employee.setAddress2(employeeDto.getAddress2());
			employee.setCity(employeeDto.getCity());
			employee.setState(employeeDto.getState());
			employee.setZipCode(employeeDto.getZipCode());
			employee.setCountry(employeeDto.getCountry());
			employee.setCompany(employeeDto.getCompany());
			employee.setStartDate(employeeDto.getStartDate());
			employee.setEndDate(employeeDto.getEndDate());
			employee.setDepartment(employeeDto.getDepartment());
			employee.setSupervisor(employeeDto.getSupervisor());
			employee.setJobTitles(employeeDto.getJobTitles());
			employee.setWorkSchedule(employeeDto.getWorkSchedule());
			employee.setStatus(employeeDto.getStatus());
			employee.setUniversity(employeeDto.getUniversity());
			employee.setDegree(employeeDto.getDegree());
			employee.setMajor(employeeDto.getMajor());
			employeeRepository.save(employee);
		} catch (Exception e) {
			throw new RuntimeException("Failed to update employee: " + e.getMessage());
		}
	}

	@Override
	public void updateEmployeeById(long id, EmployeeDto employeeDto) {
		Employee employee = getEmployeeById(id);
		updateEmployee(employee, employeeDto);
	}

	@Override
	public void updateEmployeeByEmail(String email, EmployeeDto employeeDto) {
		Employee employee = getEmployeeByEmail(email);
		updateEmployee(employee, employeeDto);
	}

	@Override
	public void deleteEmployeeById(long id) {
		try {
			employeeRepository.deleteById(id);
		} catch (Exception e) {
			// Handle any exceptions thrown during employee deletion
			throw new RuntimeException("Failed to delete employee: " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public void deleteEmployeeByEmail(String email) {
		try {
			employeeRepository.deleteByEmail(email);
		} catch (Exception e) {
			// Handle any exceptions thrown during employee deletion
			throw new RuntimeException("Failed to delete employee: " + e.getMessage());
		}
	}

	@Override
	public Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		try {
			Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
					: Sort.by(sortField).descending();

			Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
			return employeeRepository.findAll(pageable);
		} catch (Exception e) {
			// Handle any exceptions thrown during paginated employee retrieval
			throw new RuntimeException("Failed to retrieve paginated employees: " + e.getMessage());
		}
	}

	@Override
	public List<Employee> searchEmployees(String keyword, String sortField, String sortDir) {
		try {
			List<Employee> searchResult = new ArrayList<>();
			List<Employee> allEmployeesList = getAllEmployees();
			String lowercaseKeyword = keyword.toLowerCase(); // Convert the keyword to lowercase

			if (keyword.isEmpty()) {
				return searchResult; // Return an empty list without performing the search
			}

			Map<String, Function<Employee, String>> fieldToGetterMap = new HashMap<>();
			fieldToGetterMap.put("id", employee -> String.valueOf(employee.getId()));
			fieldToGetterMap.put("firstName", Employee::getFirstName);
			fieldToGetterMap.put("lastName", Employee::getLastName);
			fieldToGetterMap.put("email", Employee::getEmail);
			fieldToGetterMap.put("phoneNumber", Employee::getPhoneNumber);
			fieldToGetterMap.put("jobTitles", Employee::getJobTitles);
			fieldToGetterMap.put("department", Employee::getDepartment);
			fieldToGetterMap.put("supervisor", Employee::getSupervisor);
			fieldToGetterMap.put("status", Employee::getStatus);
			fieldToGetterMap.put("degree", Employee::getDegree);
			fieldToGetterMap.put("major", Employee::getMajor);

			Comparator<Employee> comparator;
			if (sortDir.equalsIgnoreCase("desc")) {
				comparator = Comparator.comparing(fieldToGetterMap.getOrDefault(sortField, Employee::toString))
						.reversed();
			} else {
				comparator = Comparator.comparing(fieldToGetterMap.getOrDefault(sortField, Employee::toString));
			}

			searchResult = allEmployeesList.stream()
					.filter(employee -> fieldToGetterMap.getOrDefault(sortField, Employee::toString)
							.apply(employee).toLowerCase().contains(lowercaseKeyword))
					.sorted(comparator)
					.collect(Collectors.toList());

			return searchResult;
		} catch (Exception e) {
			throw new RuntimeException("Failed to search employee: " + e.getMessage());
		}
	}
}