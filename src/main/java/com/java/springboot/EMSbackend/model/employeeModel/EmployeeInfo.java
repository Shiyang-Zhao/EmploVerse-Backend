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

    @NotNull
    @Column(name = "start_date")
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull
    @Column(name = "employment_type")
    private String employmentType;

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
        this.salaryInfo = new SalaryInfo();
    }

    public EmployeeInfo(LocalDate startDate, LocalDate endDate, String employmentType, String department,
            String manager, String jobTitles, String workLocation,
            String workSchedule, String status, SalaryInfo salaryInfo) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.employmentType = employmentType;
        this.department = department;
        this.manager = manager;
        this.jobTitles = jobTitles;
        this.workLocation = workLocation;
        this.workSchedule = workSchedule;
        this.status = status;
        this.salaryInfo = salaryInfo;
    }
}
