package com.reviewnprep.awslabs.repository;

import com.reviewnprep.awslabs.entity.LabInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabInstanceRepository extends JpaRepository <LabInstanceEntity, Long> {
}
