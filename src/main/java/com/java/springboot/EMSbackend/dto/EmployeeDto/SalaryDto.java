package com.java.springboot.EMSbackend.dto.EmployeeDto;

import java.math.BigDecimal;

public class SalaryDto {

    private Long id;
    private BigDecimal amount;
    private String payFrequency;
    private BigDecimal bonus;
    private BigDecimal taxDeduction;
    private Double overtimeHours;
    private Double overtimeRate;
    private String deductions;
    private String insuranceCoverage;

    public SalaryDto() {
    }

    public SalaryDto(Long id, BigDecimal amount, String payFrequency, BigDecimal bonus,
            BigDecimal taxDeduction, Double overtimeHours, Double overtimeRate,
            String deductions, String insuranceCoverage) {
        this.id = id;
        this.amount = amount;
        this.payFrequency = payFrequency;
        this.bonus = bonus;
        this.taxDeduction = taxDeduction;
        this.overtimeHours = overtimeHours;
        this.overtimeRate = overtimeRate;
        this.deductions = deductions;
        this.insuranceCoverage = insuranceCoverage;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public BigDecimal getTaxDeduction() {
        return taxDeduction;
    }

    public void setTaxDeduction(BigDecimal taxDeduction) {
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
