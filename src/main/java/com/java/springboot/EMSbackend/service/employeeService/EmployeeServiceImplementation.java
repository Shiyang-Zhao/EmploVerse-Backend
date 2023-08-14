package com.java.springboot.EMSbackend.service.employeeService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.java.springboot.EMSbackend.dto.EmployeeDto.EmployeeDto;
import com.java.springboot.EMSbackend.dto.EmployeeDto.SalaryDto;
import com.java.springboot.EMSbackend.model.employeeModel.Employee;
import com.java.springboot.EMSbackend.model.employeeModel.SalaryInfo;
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
			employeeDto.getPersonalInfo().setSsn(passwordEncoder.encode(employeeDto.getPersonalInfo().getSsn()));
			Employee newEmployee = new Employee(employeeDto.getUser(), employeeDto.getPersonalInfo(),
					employeeDto.getEmployeeInfo(), employeeDto.getEducationInfo());
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
			employeeDto.getPersonalInfo().setSsn(passwordEncoder.encode(employeeDto.getPersonalInfo().getSsn()));
			employee.setUser(employeeDto.getUser());
			employee.setPersonalInfo(employeeDto.getPersonalInfo());
			employee.setEmployeeInfo(employeeDto.getEmployeeInfo());
			employee.setEducationInfo(employeeDto.getEducationInfo());
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
			// Handle any exceptions thrown during employee deletion
			throw new RuntimeException("Failed to delete employee: " + e.getMessage());
		}
	}

	@Override
	public Page<Employee> getPaginatedEmployees(int pageNo, int pageSize, String sortField,
			String sortDir) {
		try {
			Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
					: Sort.by(sortField).descending();

			Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
			// int startIndex = (int) pageable.getOffset();
			// int endIndex = Math.min(startIndex + pageable.getPageSize(), employeeList.size());
			// List<Employee> paginatedList = employeeList.subList(startIndex, endIndex);
			return employeeRepository.findAll(pageable);
		} catch (Exception e) {
			// Handle any exceptions thrown during paginated employee retrieval
			throw new RuntimeException("Failed to retrieve paginated employees: " + e.getMessage());
		}
	}

	private Function<Employee, String> createFieldToGetterMap(String searchField) {
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

		return fieldToGetterMap.get(searchField);
	}

	@Override
	public List<Employee> searchEmployees(String keyword, String searchField) {
		try {
			List<Employee> allEmployeesList = getAllEmployees();
			String lowercaseKeyword = keyword.toLowerCase(); // Convert the keyword to lowercase

			if (keyword.isEmpty()) {
				// If the search keyword is empty, return all employees using findPaginated
				// method
				return getAllEmployees();
			}

			Function<Employee, String> getter = createFieldToGetterMap(searchField);

			if (getter == null) {
				throw new IllegalArgumentException("Invalid searchField: " + searchField);
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

	// @Override
	// public List<Employee> sortEmployees(List<Employee> employees, String
	// sortField, String sortDir) {
	// try {
	// Function<Employee, String> getter = createFieldToGetterMap(sortField);
	// if (getter == null) {
	// throw new IllegalArgumentException("Invalid sortField: " + sortField);
	// }

	// Comparator<Employee> comparator = Comparator.comparing(getter);
	// if (sortDir.equalsIgnoreCase("desc")) {
	// comparator = comparator.reversed();
	// }

	// return employees.stream()
	// .sorted(comparator)
	// .collect(Collectors.toList());
	// } catch (Exception e) {
	// throw new RuntimeException("Failed to sort employees: " + e.getMessage());
	// }
	// }

	public void setSalaryDetails(SalaryInfo salaryInfo, SalaryDto salaryDto) {
		salaryInfo.setAmount(salaryDto.getAmount());
		salaryInfo.setPayFrequency(salaryDto.getPayFrequency());
		salaryInfo.setBonus(salaryDto.getBonus());
		salaryInfo.setTaxDeduction(salaryDto.getTaxDeduction());
		salaryInfo.setOvertimeHours(salaryDto.getOvertimeHours());
		salaryInfo.setOvertimeRate(salaryDto.getOvertimeRate());
		salaryInfo.setDeductions(salaryDto.getDeductions());
		salaryInfo.setInsuranceCoverage(salaryDto.getInsuranceCoverage());
	}

	@Override
	public BigDecimal setNetSalaryById(long id, SalaryDto salaryDto) {
		Employee employee = getEmployeeById(id);
		SalaryInfo salaryInfo = employee.getEmployeeInfo().getSalaryInfo();
		setSalaryDetails(salaryInfo, salaryDto);
		// Set the values from the SalaryDTO to the SalaryInfo object

		BigDecimal baseSalary = salaryInfo.getAmount();
		BigDecimal bonusAmount = salaryInfo.getBonus();
		BigDecimal taxDeductionAmount = salaryInfo.getTaxDeduction();
		double overtimeHours = salaryInfo.getOvertimeHours();
		double overtimeRate = salaryInfo.getOvertimeRate();

		// Calculate overtime pay
		BigDecimal overtimePay = BigDecimal.valueOf(overtimeHours * overtimeRate);

		// Calculate gross salary
		BigDecimal grossSalary = baseSalary.add(bonusAmount).add(overtimePay);

		// Calculate net salary after tax deductions
		BigDecimal netSalary = grossSalary.subtract(taxDeductionAmount);

		return netSalary;
	}

}