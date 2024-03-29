package com.java.springboot.EMSbackend.model.employeeModel;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "personal_info")
public class PersonalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    // Personal information
    //@notnull
    @Column(name = "gender")
    private String gender;

    //@notnull
    @Column(name = "birthday")
    private LocalDate birthday;

    //@notnull
    @Column(name = "citizenship")
    private String citizenship;

    //@notnull
    // @Pattern(regexp = "^[0-9]{3}-[0-9]{2}-[0-9]{4}$")
    @Column(name = "ssn")
    private String ssn;

    //@notnull
    @Column(name = "address1")
    private String address1;

    //@notnull
    @Column(name = "address2")
    private String address2;

    //@notnull
    @Column(name = "city")
    private String city;

    //@notnull
    @Column(name = "state")
    private String state;

    //@notnull
    // @Pattern(regexp = "^\\d{5}(?:-\\d{4})?$")
    @Column(name = "zip_code")
    private String zipCode;

    //@notnull
    @Column(name = "country")
    private String country;

    // Constructors
    public PersonalInfo() {
        this.gender = "N/A";
        this.birthday = LocalDate.now();
        this.citizenship = "N/A";
        this.ssn = "123-45-6789";
        this.address1 = "N/A";
        this.address2 = "N/A";
        this.city = "Unknown";
        this.state = "Unknown";
        this.zipCode = "12345";
        this.country = "N/A";
    }

    public PersonalInfo(String gender, LocalDate birthday, String citizenship, String ssn, String address1,
            String address2,
            String city, String state, String zipCode, String country) {
        this.gender = gender;
        this.birthday = birthday;
        this.citizenship = citizenship;
        this.ssn = ssn;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }
}
