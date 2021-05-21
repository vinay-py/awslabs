package com.reviewnprep.awslabs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabDto {
    Long labId;
    String labName;
    String labDescription;
    String stackLocation;
    String stackName;
    String userGroupName;

    public LabDto(String labName, String labDescription, String stackLocation) {
        this.labName = labName;
        this.labDescription = labDescription;
        this.stackLocation = stackLocation;
    }
}
