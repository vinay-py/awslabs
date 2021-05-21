package com.reviewnprep.awslabs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabInstanceDto {
    Long labInstanceId;
    String labName;
    String stackName;
    String userName;
    int durationInSeconds;
    LocalDate createDate;
    LocalTime createTime;
    boolean isActive;

    public LabInstanceDto(String labName, int durationInSeconds) {
        this.labName = labName;
        this.durationInSeconds = durationInSeconds;
    }
}
