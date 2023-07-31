package com.java.springboot.EMSbackend.dto;

import java.time.LocalDate;

import com.java.springboot.EMSbackend.model.userModel.User;

public class EmployeeDto {
	private User user;
	private String ssn;
	private LocalDate birthday;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	private String company;
	private LocalDate startDate;
	private LocalDate endDate;
	private String department;
	private String supervisor;
	private String jobTitles;
	private String workSchedule;
	private String status;
	private String university;
	private String degree;
	private String major;

	public EmployeeDto() {

	}

	// Constructor
	public EmployeeDto(User user, String ssn, LocalDate birthday, String address1, String address2,
			String city, String state, String zipCode, String country, String company,
			LocalDate startDate, LocalDate endDate, String department, String supervisor,
			String jobTitles, String workSchedule, String status, String university,
			String degree, String major) {
		this.user = user;
		this.ssn = ssn;
		this.birthday = birthday;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
		this.company = company;
		this.startDate = startDate;
		this.endDate = endDate;
		this.department = department;
		this.supervisor = supervisor;
		this.jobTitles = jobTitles;
		this.workSchedule = workSchedule;
		this.status = status;
		this.university = university;
		this.degree = degree;
		this.major = major;
	}

	// Getters
	public User getUser() {
		return user;
	}

	public String getSsn() {
		return ssn;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public String getAddress1() {
		return address1;
	}

	public String getAddress2() {
		return address2;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getCountry() {
		return country;
	}

	public String getCompany() {
		return company;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public String getDepartment() {
		return department;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public String getJobTitles() {
		return jobTitles;
	}

	public String getWorkSchedule() {
		return workSchedule;
	}

	public String getStatus() {
		return status;
	}

	public String getUniversity() {
		return university;
	}

	public String getDegree() {
		return degree;
	}

	public String getMajor() {
		return major;
	}

	// Setters
	public void setUser(User user) {
		this.user = user;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public void setJobTitles(String jobTitles) {
		this.jobTitles = jobTitles;
	}

	public void setWorkSchedule(String workSchedule) {
		this.workSchedule = workSchedule;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public void setMajor(String major) {
		this.major = major;
	}

}