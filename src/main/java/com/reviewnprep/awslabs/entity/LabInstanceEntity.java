package com.reviewnprep.awslabs.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class LabInstanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lab_id", nullable = false)
    private LabEntity lab;

    String stackName;
    String userName;
    int durationInSeconds;
    LocalDate createDate;
    LocalTime createTime;
    boolean isActive = true;

    public LabInstanceEntity(LabEntity lab, int durationInSeconds) {
        this.lab = lab;
        this.durationInSeconds = durationInSeconds;

        this.createDate = LocalDate.now();
        this.createTime = LocalTime.now();

    }
}
