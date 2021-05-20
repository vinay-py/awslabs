package com.reviewnprep.awslabs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabDto {
    String labName;
    String labDescription;
    String stackLocation;
//    String stackName;
//    String userGroupName;
}
