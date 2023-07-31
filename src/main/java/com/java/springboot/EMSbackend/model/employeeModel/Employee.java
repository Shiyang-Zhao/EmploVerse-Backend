package com.java.springboot.EMSbackend.model.employeeModel;

import com.java.springboot.EMSbackend.model.userModel.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "employees")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private long id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@NotNull
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@OneToOne(optional = false, cascade = CascadeType.ALL)
	@NotNull
	@JoinColumn(name = "personalInfo_id", referencedColumnName = "id")
	private PersonalInfo personalInfo;

	@OneToOne(optional = false, cascade = CascadeType.ALL)
	@NotNull
	@JoinColumn(name = "employeeInfo_id", referencedColumnName = "id")
	private EmployeeInfo employeeInfo;

	@OneToOne(optional = false, cascade = CascadeType.ALL)
	@NotNull
	@JoinColumn(name = "educationInfo_id", referencedColumnName = "id")
	private EducationInfo educationInfo;

	// Constructors, getters, and setters...
	public Employee() {
	}

	// Constructor without ID (for creating new employees, ID will be generated by
	// the database)
	public Employee(User user) {
		this.user = user;
	}

	public Employee(User user, PersonalInfo personalInfo, EmployeeInfo employeeInfo, EducationInfo educationInfo) {
		this.user = user;
		this.personalInfo = personalInfo;
		this.employeeInfo = employeeInfo;
		this.educationInfo = educationInfo;
	}

	// Getters and Setters
	public long getId() {
		return id;
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