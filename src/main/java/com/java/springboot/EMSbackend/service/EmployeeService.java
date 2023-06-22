package com.java.springboot.EMSbackend.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.java.springboot.EMSbackend.dto.EmployeeDto;
import com.java.springboot.EMSbackend.model.Employee;

public interface EmployeeService {

	List<Employee> getAllEmployees();

	void saveEmployee(EmployeeDto employeeDto);

	Employee getEmployeeById(long id);

	Employee getEmployeeByEmail(String email);

	void updateEmployeeById(long id, EmployeeDto employeeDto);

	void updateEmployeeByEmail(String email, EmployeeDto employeeDto);

	void deleteEmployeeById(long id);

	void deleteEmployeeByEmail(String email);

	Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

	List<Employee> searchEmployees(String keyword, String sortField, String sortDirection);
}