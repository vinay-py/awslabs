package com.reviewnprep.awslabs.repository;

import com.reviewnprep.awslabs.entity.LabEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabRepository extends JpaRepository <LabEntity, Long> {
}
