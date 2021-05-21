package com.reviewnprep.awslabs.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class LabEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;
    String labDescription;
    String stackLocation;
    String stackName;
    String userGroupName;

    public LabEntity(String labName, String labDescription, String stackLocation) {
        this.name = labName;
        this.labDescription = labDescription;
        this.stackLocation = stackLocation;
    }
}
