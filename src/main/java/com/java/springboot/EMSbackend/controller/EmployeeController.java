package com.java.springboot.EMSbackend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.springboot.EMSbackend.dto.EmployeeDto;
import com.java.springboot.EMSbackend.model.Employee;
import com.java.springboot.EMSbackend.service.EmployeeService;

@RestController
@CrossOrigin(origins = { "http://localhost:3000",
		"http://emploverse-frontend.herokuapp.com/" })
@RequestMapping("/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		List<Employee> employeesList = employeeService.getAllEmployees();
		return ResponseEntity.ok(employeesList);
	}

	@PostMapping("/saveEmployee")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<String> saveEmployee(@RequestBody EmployeeDto employeeDto) {
		employeeService.saveEmployee(employeeDto);
		return ResponseEntity.ok("Employee is saved successfully!!!");
	}

	@GetMapping("/getEmployeeById/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") long id) {
		Employee employee = employeeService.getEmployeeById(id);
		return ResponseEntity.ok(employee);
	}

	@GetMapping("/getEmployeeByEmail/{email}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
	public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable(value = "email") String email) {
		Employee employee = employeeService.getEmployeeByEmail(email);
		return ResponseEntity.ok(employee);
	}

	@PostMapping("/updateEmployeeById/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<String> updateEmployeeById(@PathVariable(value = "id") long id,
			@RequestBody EmployeeDto employeeDto) {
		employeeService.updateEmployeeById(id, employeeDto);
		return ResponseEntity.ok("Employee is updated successfully!!!");
	}

	@PostMapping("/updateEmployeeByEmail/{email}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<String> updateEmployeeByEmail(@PathVariable(value = "email") String email,
			@RequestBody EmployeeDto employeeDto) {
		employeeService.updateEmployeeByEmail(email, employeeDto);
		return ResponseEntity.ok("Employee is updated successfully!!!");
	}

	@PostMapping("/deleteEmployeeById/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ResponseEntity<String> deleteEmployeeById(@PathVariable(value = "id") long id) {
		employeeService.deleteEmployeeById(id);
		return ResponseEntity.ok("Employee is deleted successfully!!!");
	}

	@PostMapping("/deleteEmployeeByEmail/{email}")
	public ResponseEntity<String> deleteEmployeeByEmail(@PathVariable(value = "email") String email) {
		employeeService.deleteEmployeeByEmail(email);
		return ResponseEntity.ok("Employee is deleted successfully!!!");
	}

	@GetMapping("/page/{pageNo}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
	public ResponseEntity<Map<String, Object>> findPaginated(@PathVariable(value = "pageNo") int pageNo,
			@RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
			@RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {
		int pageSize = 3;
		Page<Employee> page = employeeService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Employee> listEmployees = page.getContent();

		Map<String, Object> response = new HashMap<>();
		response.put("currentPage", pageNo);
		response.put("totalPages", page.getTotalPages());
		response.put("totalItems", page.getTotalElements());
		response.put("sortField", sortField);
		response.put("sortDir", sortDir);
		response.put("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		response.put("listEmployees", listEmployees);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/searchEmployees")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
	public ResponseEntity<List<Employee>> searchEmployees(
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
			@RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {
		List<Employee> searchResult = employeeService.searchEmployees(keyword, sortField, sortDir);
		return ResponseEntity.ok(searchResult);
	}
}