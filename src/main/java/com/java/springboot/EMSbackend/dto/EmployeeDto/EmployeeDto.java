package com.java.springboot.EMSbackend.dto.EmployeeDto;

import com.java.springboot.EMSbackend.model.employeeModel.EducationInfo;
import com.java.springboot.EMSbackend.model.employeeModel.EmployeeInfo;
import com.java.springboot.EMSbackend.model.employeeModel.PersonalInfo;
import com.java.springboot.EMSbackend.model.employeeModel.SalaryInfo;
import com.java.springboot.EMSbackend.model.userModel.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
	private User user;
	private PersonalInfo personalInfo;
	private EmployeeInfo employeeInfo;
	private EducationInfo educationInfo;
	private SalaryInfo salaryInfo;

	public EmployeeDto(User user) {
		this.user = user;
	}
}