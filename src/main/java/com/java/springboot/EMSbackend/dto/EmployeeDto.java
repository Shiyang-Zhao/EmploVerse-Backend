package com.java.springboot.EMSbackend.dto;

public class EmployeeDto {
	private String firstName;
	private String lastName;
	private String email;
	private String birthday;
	private String phoneNumber;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	private String company;
	private String startDate;
	private String endDate;
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

	public EmployeeDto(String firstName, String lastName, String email, String birthday, String phoneNumber,
			String address1, String address2, String city, String state, String zipCode, String country, String company,
			String startDate,
			String endDate,
			String department, String supervisor, String jobTitles, String workSchedule, String status,
			String university, String degree, String major) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.birthday = birthday;
		this.phoneNumber = phoneNumber;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getJobTitles() {
		return jobTitles;
	}

	public void setJobTitles(String jobTitles) {
		this.jobTitles = jobTitles;
	}

	public String getWorkSchedule() {
		return workSchedule;
	}

	public void setWorkSchedule(String workSchedule) {
		this.workSchedule = workSchedule;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}
}