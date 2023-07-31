package com.java.springboot.EMSbackend.model.employeeModel;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "salary_info")
public class SalaryInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @NotNull
    @Column(name = "amount")
    private BigDecimal amount; // Assuming salary is a numerical value represented as a double.

    @NotNull
    @Column(name = "pay_frequency")
    private String payFrequency; // Monthly, bi-weekly, etc.

    @NotNull
    @Column(name = "bonus")
    private Double bonus; // Any bonus amount associated with the salary.

    @NotNull
    @Column(name = "tax_deduction")
    private Double taxDeduction; // The amount deducted as taxes from the salary.

    @NotNull
    @Column(name = "overtime_hours")
    private Double overtimeHours; // Number of overtime hours worked.

    @NotNull
    @Column(name = "overtime_rate")
    private Double overtimeRate; // Rate of pay for overtime hours.

    @NotNull
    @Column(name = "deductions")
    private String deductions; // Any additional deductions made from the salary.

    @NotNull
    @Column(name = "insurance_coverage")
    private String insuranceCoverage; // Details of insurance coverage provided by the employer.

    public SalaryInfo() {
    }

    public SalaryInfo(BigDecimal amount, String payFrequency,
            Double bonus, Double taxDeduction, Double overtimeHours, Double overtimeRate, String deductions,
            String insuranceCoverage) {
        this.amount = amount;
        this.payFrequency = payFrequency;
        this.bonus = bonus;
        this.taxDeduction = taxDeduction;
        this.overtimeHours = overtimeHours;
        this.overtimeRate = overtimeRate;
        this.deductions = deductions;
        this.insuranceCoverage = insuranceCoverage;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayFrequency() {
        return payFrequency;
    }

    public void setPayFrequency(String payFrequency) {
        this.payFrequency = payFrequency;
    }

    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    public Double getTaxDeduction() {
        return taxDeduction;
    }

    public void setTaxDeduction(Double taxDeduction) {
        this.taxDeduction = taxDeduction;
    }

    public Double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(Double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public Double getOvertimeRate() {
        return overtimeRate;
    }

    public void setOvertimeRate(Double overtimeRate) {
        this.overtimeRate = overtimeRate;
    }

    public String getDeductions() {
        return deductions;
    }

    public void setDeductions(String deductions) {
        this.deductions = deductions;
    }

    public String getInsuranceCoverage() {
        return insuranceCoverage;
    }

    public void setInsuranceCoverage(String insuranceCoverage) {
        this.insuranceCoverage = insuranceCoverage;
    }
}
