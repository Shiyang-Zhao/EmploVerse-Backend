package com.java.springboot.EMSbackend.model.employeeModel;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "employee_info")
public class EmployeeInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @NotNull
    @Column(name = "start_date")
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull
    @Column(name = "department")
    private String department;

    @NotNull
    @Column(name = "manager")
    private String manager;

    @NotNull
    @Column(name = "job_titles")
    private String jobTitles;

    @NotNull
    @Column(name = "work_location")
    private String workLocation;

    @NotNull
    @Column(name = "work_schedule")
    private String workSchedule;

    @NotNull
    @Column(name = "status")
    private String status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "salary_info_id", referencedColumnName = "id")
    private SalaryInfo salaryInfo;

    // Constructors
    public EmployeeInfo() {
    }

    public EmployeeInfo(LocalDate startDate, LocalDate endDate, String department, String manager,
            String jobTitles, String workLocation, String workSchedule, String status) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.department = department;
        this.manager = manager;
        this.jobTitles = jobTitles;
        this.workLocation = workLocation;
        this.workSchedule = workSchedule;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getJobTitles() {
        return jobTitles;
    }

    public void setJobTitles(String jobTitles) {
        this.jobTitles = jobTitles;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
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
}
