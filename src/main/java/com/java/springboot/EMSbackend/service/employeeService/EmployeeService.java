package com.java.springboot.EMSbackend.service.employeeService;

import java.util.List;

import org.springframework.data.domain.Page;

import com.java.springboot.EMSbackend.dto.EmployeeDto;
import com.java.springboot.EMSbackend.model.employeeModel.Employee;

public interface EmployeeService {

	List<Employee> getAllEmployees();

	void createEmployee(EmployeeDto employeeDto);

	Employee getEmployeeById(long id);

	void updateEmployeeById(long id, EmployeeDto employeeDto);

	void deleteEmployeeById(long id);

	Page<Employee> getPaginatedEmployees(List<Employee> employeeList, int pageNo, int pageSize, String sortField,
			String sortDir);

	List<Employee> searchEmployees(String keyword, String searchField);

	// List<Employee> sortEmployees(List<Employee> employees, String sortField,
	// String sortDir);
}