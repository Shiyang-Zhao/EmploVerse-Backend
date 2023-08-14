package com.java.springboot.EMSbackend.model.employeeModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "education_info")
public class EducationInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;
    // Education information
    @NotNull
    @Column(name = "university")
    private String university;

    @NotNull
    @Column(name = "degree")
    private String degree;

    @NotNull
    @Column(name = "major")
    private String major;

    @NotNull
    @Column(name = "gpa")
    private String gpa;

    // Constructors
    public EducationInfo() {
        this.university = "N/A";
        this.degree = "N/A";
        this.major = "N/A";
        this.gpa = "N/A";
    }

    public EducationInfo(String university, String degree, String major, String gpa) {
        this.university = university;
        this.degree = degree;
        this.major = major;
        this.gpa = gpa;
    }

    // Getters and Setters
    public Long getId() {
        return id;
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

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }
}
