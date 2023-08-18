package com.java.springboot.EMSbackend.dto.EmployeeDto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SalaryDto {
    private BigDecimal amount;
    private String payFrequency;
    private BigDecimal bonus;
    private BigDecimal taxDeduction;
    private Double overtimeHours;
    private Double overtimeRate;
    private String deductions;
    private String insuranceCoverage;
}
