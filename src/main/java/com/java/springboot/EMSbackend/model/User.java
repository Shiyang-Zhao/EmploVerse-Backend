package com.java.springboot.EMSbackend.model;

import java.util.Collection;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "Email"),
		@UniqueConstraint(columnNames = "Phone Number") })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private long id;
	// Account information
	@Column(name = "First Name", nullable = false)
	private String firstName;
	@Column(name = "Last Name", nullable = false)
	private String lastName;
	@Column(name = "Email", nullable = false)
	private String email;
	@Column(name = "Password", nullable = false)
	private String password;
	// Personal information
	@Column(name = "Birthday", nullable = false)
	private String birthday;
	@Column(name = "Phone Number", nullable = false)
	private String phoneNumber;
	@Column(name = "Address1", nullable = false)
	private String address1;
	@Column(name = "Address2", nullable = false)
	private String address2;
	@Column(name = "City", nullable = false)
	private String city;
	@Column(name = "State", nullable = false)
	private String state;
	@Column(name = "Zip Code", nullable = false)
	private String zipCode;
	@Column(name = "Country", nullable = false)
	private String country;
	@Column(name = "Company", nullable = false)
	private String company;
	@Column(name = "Start Date", nullable = false)
	private String startDate;
	@Column(name = "End Date", nullable = false)
	private String endDate;
	@Column(name = "Department", nullable = false)
	private String department;
	@Column(name = "Supervisor", nullable = false)
	private String supervisor;
	@Column(name = "Job Titles", nullable = false)
	private String jobTitles;
	@Column(name = "Work Schedule", nullable = false)
	private String workSchedule;
	@Column(name = "Status", nullable = false)
	private String status;
	// Education information
	@Column(name = "University", nullable = false)
	private String university;
	@Column(name = "Degree", nullable = false)
	private String degree;
	@Column(name = "Major", nullable = false)
	private String major;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Collection<Role> roles;

	public User() {

	}

	public User(String firstName, String lastName, String email, String password, String birthday,
			String phoneNumber, String address1, String address2, String city, String state, String zipCode,
			String country, String company, String startDate, String endDate, String department, String supervisor,
			String jobTitles, String workSchedule,
			String status, String university, String degree, String major, Collection<Role> roles) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
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
		this.roles = roles;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
}