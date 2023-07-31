package com.java.springboot.EMSbackend.model.employeeModel;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "personal_info")
public class PersonalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    // Personal information
    @NotNull
    @Column(name = "gender")
    private String gender;

    @NotNull
    @Column(name = "birthday")
    private LocalDate birthday;

    @NotNull
    @Column(name = "citizenship")
    private String citizenship;

    @NotNull
    @Pattern(regexp = "^[0-9]{3}-[0-9]{2}-[0-9]{4}$")
    @Column(name = "ssn")
    private String ssn;

    @NotNull
    @Column(name = "address1")
    private String address1;

    @NotNull
    @Column(name = "address2")
    private String address2;

    @NotNull
    @Column(name = "city")
    private String city;

    @NotNull
    @Column(name = "state")
    private String state;

    @NotNull
    @Pattern(regexp = "^\\d{5}(?:-\\d{4})?$")
    @Column(name = "zip_code")
    private String zipCode;

    @NotNull
    @Column(name = "country")
    private String country;

    // Constructors
    public PersonalInfo() {
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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
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
}
