package com.java.springboot.EMSbackend.dto.EmployeeDto;

import com.java.springboot.EMSbackend.model.employeeModel.EducationInfo;
import com.java.springboot.EMSbackend.model.employeeModel.EmployeeInfo;
import com.java.springboot.EMSbackend.model.employeeModel.PersonalInfo;
import com.java.springboot.EMSbackend.model.userModel.User;

public class EmployeeDto {
	private User user;
	private PersonalInfo personalInfo;
	private EmployeeInfo employeeInfo;
	private EducationInfo educationInfo;

	public EmployeeDto() {
	}

	public EmployeeDto(User user) {
		this.user = user;
	}

	public EmployeeDto(PersonalInfo personalInfo, EmployeeInfo employeeInfo, EducationInfo educationInfo) {
		this.personalInfo = personalInfo;
		this.employeeInfo = employeeInfo;
		this.educationInfo = educationInfo;
	}

	public EmployeeDto(User user, PersonalInfo personalInfo, EmployeeInfo employeeInfo, EducationInfo educationInfo) {
		this.user = user;
		this.personalInfo = personalInfo;
		this.employeeInfo = employeeInfo;
		this.educationInfo = educationInfo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PersonalInfo getPersonalInfo() {
		return personalInfo;
	}

	public void setPersonalInfo(PersonalInfo personalInfo) {
		this.personalInfo = personalInfo;
	}

	public EmployeeInfo getEmployeeInfo() {
		return employeeInfo;
	}

	public void setEmployeeInfo(EmployeeInfo employeeInfo) {
		this.employeeInfo = employeeInfo;
	}

	public EducationInfo getEducationInfo() {
		return educationInfo;
	}

	public void setEducationInfo(EducationInfo educationInfo) {
		this.educationInfo = educationInfo;
	}

}