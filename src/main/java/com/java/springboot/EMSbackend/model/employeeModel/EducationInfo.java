package com.java.springboot.EMSbackend.model.employeeModel;

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
@Table(name = "education_info")
public class EducationInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    // Education information
    //@notnull
    @Column(name = "university")
    private String university;

    //@notnull
    @Column(name = "degree")
    private String degree;

    //@notnull
    @Column(name = "major")
    private String major;

    //@notnull
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
}
