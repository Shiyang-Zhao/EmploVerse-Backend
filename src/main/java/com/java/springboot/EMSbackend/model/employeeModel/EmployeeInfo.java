package com.java.springboot.EMSbackend.model.employeeModel;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "employee_info")
public class EmployeeInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    //@notnull
    @Column(name = "start_date")
    private LocalDate startDate;

    //@notnull
    @Column(name = "end_date")
    private LocalDate endDate;

    //@notnull
    @Column(name = "employment_type")
    private String employmentType;

    //@notnull
    @Column(name = "department")
    private String department;

    //@notnull
    @Column(name = "manager")
    private String manager;

    //@notnull
    @Column(name = "job_titles")
    private String jobTitles;

    //@notnull
    @Column(name = "work_location")
    private String workLocation;

    //@notnull
    @Column(name = "work_schedule")
    private String workSchedule;

    //@notnull
    @Column(name = "status")
    private String status;

    public EmployeeInfo() {
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now();
        this.employmentType = "N/A";
        this.department = "N/A";
        this.manager = "N/A";
        this.jobTitles = "N/A";
        this.workLocation = "N/A";
        this.workSchedule = "N/A";
        this.status = "N/A";
    }

    public EmployeeInfo(LocalDate startDate, LocalDate endDate, String employmentType, String department,
            String manager, String jobTitles, String workLocation,
            String workSchedule, String status) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.employmentType = employmentType;
        this.department = department;
        this.manager = manager;
        this.jobTitles = jobTitles;
        this.workLocation = workLocation;
        this.workSchedule = workSchedule;
        this.status = status;
    }
}
