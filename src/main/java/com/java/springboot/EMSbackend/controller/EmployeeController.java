package com.java.springboot.EMSbackend.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.springboot.EMSbackend.dto.EmployeeDto.EmployeeDto;
import com.java.springboot.EMSbackend.dto.EmployeeDto.SalaryDto;
import com.java.springboot.EMSbackend.model.employeeModel.Employee;
import com.java.springboot.EMSbackend.service.employeeService.EmployeeService;

@RestController
// @CrossOrigin(origins = { "http://localhost:3000",
// 		"http://emploverse-frontend.herokuapp.com/" }, allowCredentials = "true")
@RequestMapping("/employees")
public class EmployeeController {

	private final EmployeeService employeeService;

	@Autowired
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping("/")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		List<Employee> employeesList = employeeService.getAllEmployees();
		return ResponseEntity.ok(employeesList);
	}

	// Only Admin and Manager can add employees
	@PostMapping("/createEmployee")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<String> createEmployee(@RequestBody EmployeeDto employeeDto) {
		employeeService.createEmployee(employeeDto);
		return ResponseEntity.ok("Employee is saved successfully!!!");
	}

	@GetMapping("/getEmployeeById/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") long id) {
		Employee employee = employeeService.getEmployeeById(id);
		return ResponseEntity.ok(employee);
	}

	// Only Admin and Manager can update employees
	@PostMapping("/updateEmployeeById/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<String> updateEmployeeById(@PathVariable(value = "id") long id,
			@RequestBody EmployeeDto employeeDto) {
		employeeService.updateEmployeeById(id, employeeDto);
		return ResponseEntity.ok("Employee is updated successfully!!!");
	}

	// Only Admin and Manager can delete employees
	@PostMapping("/deleteEmployeeById/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<String> deleteEmployeeById(@PathVariable(value = "id") long id) {
		employeeService.deleteEmployeeById(id);
		return ResponseEntity.ok("Employee is deleted successfully!!!");
	}

	@GetMapping("/page/{pageNo}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
	public ResponseEntity<Map<String, Object>> getPaginatedEmployees(
			@PathVariable(value = "pageNo") int pageNo,
			@RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
			@RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {
		int pageSize = 3;
		Page<Employee> page = employeeService.getPaginatedEmployees(pageNo, pageSize, sortField, sortDir);

		Map<String, Object> response = new HashMap<>();
		response.put("currentPage", pageNo);
		response.put("totalPages", page.getTotalPages());
		response.put("totalItems", page.getTotalElements());
		response.put("sortField", sortField);
		response.put("sortDir", sortDir);
		response.put("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		response.put("employeeList", page.getContent());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/searchEmployees")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
	public ResponseEntity<List<Employee>> searchEmployees(
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "searchField", required = false, defaultValue = "id") String searchField) {
		List<Employee> searchResult = employeeService.searchEmployees(keyword, searchField);
		return ResponseEntity.ok(searchResult);
	}

	@GetMapping("/setNetSalaryById/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<String> getEmployeeNetSalaryById(@PathVariable(value = "id") long id,
			@RequestBody SalaryDto salaryDto) {
		BigDecimal netSalary = employeeService.setNetSalaryById(id, salaryDto);
		String responseMessage = String.format("Salary of employee with id %d: $%f", id, netSalary);
		return ResponseEntity.ok(responseMessage);
	}
}