package com.java.springboot.EMSbackend.service.employeeService;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.java.springboot.EMSbackend.dto.EmployeeDto.EmployeeDto;
import com.java.springboot.EMSbackend.model.employeeModel.Employee;
import com.java.springboot.EMSbackend.repository.EmployeeRepository;

@Service
public class EmployeeServiceImplementation implements EmployeeService {

	private final BCryptPasswordEncoder passwordEncoder;

	private final EmployeeRepository employeeRepository;

	@Autowired
	public EmployeeServiceImplementation(BCryptPasswordEncoder passwordEncoder, EmployeeRepository employeeRepository) {
		this.passwordEncoder = passwordEncoder;
		this.employeeRepository = employeeRepository;
	}

	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public void createEmployee(EmployeeDto employeeDto) {
		try {
			Employee newEmployee = new Employee(employeeDto.getUser(), employeeDto.getPersonalInfo(),
					employeeDto.getEmployeeInfo(), employeeDto.getEducationInfo(), employeeDto.getSalaryInfo());
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

	private void updateEmployee(Employee employee, EmployeeDto employeeDto) {
		try {
			employee.setUser(employeeDto.getUser());
			employee.setPersonalInfo(employeeDto.getPersonalInfo());
			employee.setEmployeeInfo(employeeDto.getEmployeeInfo());
			employee.setEducationInfo(employeeDto.getEducationInfo());
			employee.setSalaryInfo(employeeDto.getSalaryInfo());
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
	public void deleteEmployeeById(long id) {
		try {
			employeeRepository.deleteById(id);
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete employee: " + e.getMessage());
		}
	}

	private Function<Employee, String> createFieldToGetterMap(String field) {
		Map<String, Function<Employee, String>> fieldToGetterMap = new HashMap<>();
		fieldToGetterMap.put("id", employee -> String.valueOf(employee.getId()));
		fieldToGetterMap.put("firstName", employee -> employee.getUser().getFirstName());
		fieldToGetterMap.put("lastName", employee -> employee.getUser().getLastName());
		fieldToGetterMap.put("username", employee -> employee.getUser().getUsername());
		fieldToGetterMap.put("email", employee -> employee.getUser().getEmail());
		fieldToGetterMap.put("phoneNumber", employee -> employee.getUser().getPhoneNumber());
		fieldToGetterMap.put("jobTitles", employee -> employee.getEmployeeInfo().getJobTitles());
		fieldToGetterMap.put("department", employee -> employee.getEmployeeInfo().getDepartment());
		fieldToGetterMap.put("manager", employee -> employee.getEmployeeInfo().getManager());

		return fieldToGetterMap.get(field);
	}

	@Override
	public Page<Employee> getPaginatedEmployees(int pageNo, int pageSize, String sortField, String sortDir) {
		try {
			List<Employee> allEmployees = getAllEmployees();

			if (sortField != null && !sortField.isEmpty()) {
				Function<Employee, String> getter = createFieldToGetterMap(sortField);

				if (getter != null) {
					Comparator<Employee> comparator;
					if ("id".equals(sortField)) {
						comparator = Comparator.comparingLong(employee -> employee.getId());
					} else {
						comparator = Comparator.comparing(getter);
					}

					if ("desc".equalsIgnoreCase(sortDir)) {
						comparator = comparator.reversed();
					}
					allEmployees.sort(comparator);
				}
			}

			int fromIndex = (pageNo - 1) * pageSize;
			int toIndex = Math.min(fromIndex + pageSize, allEmployees.size());
			List<Employee> paginatedEmployees = allEmployees.subList(fromIndex, toIndex);

			return new PageImpl<>(paginatedEmployees, PageRequest.of(pageNo - 1, pageSize), allEmployees.size());
		} catch (Exception e) {
			throw new RuntimeException("Failed to retrieve paginated employees: " + e.getMessage());
		}
	}

	@Override
	public List<Employee> searchEmployees(String keyword, String searchField) {
		try {
			List<Employee> allEmployeesList = getAllEmployees();
			String lowercaseKeyword = keyword.toLowerCase();

			if (keyword.isEmpty()) {
				return new ArrayList<>();
			}

			Function<Employee, String> getter = createFieldToGetterMap(searchField);

			if (getter == null) {
				throw new IllegalArgumentException("Invalid search field: " + searchField);
			}

			// Define a predicate to filter employees based on the keyword and search field
			Predicate<Employee> searchPredicate = employee -> getter.apply(employee).toLowerCase()
					.contains(lowercaseKeyword);

			// Apply the search predicate to filter the employees
			List<Employee> searchResult = allEmployeesList.stream().filter(searchPredicate)
					.collect(Collectors.toList());

			return searchResult;
		} catch (Exception e) {
			throw new RuntimeException("Failed to search employee: " + e.getMessage());
		}
	}

}