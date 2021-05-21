package com.reviewnprep.awslabs.service;

import com.reviewnprep.awslabs.awsall.CreateStack;
import com.reviewnprep.awslabs.dto.LabDto;
import com.reviewnprep.awslabs.entity.LabEntity;
import com.reviewnprep.awslabs.repository.LabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LabService {

    @Autowired
    LabRepository labRepository;

    public LabDto createLab(LabDto labDto) {
        LabDto responseLabDto;

        LabEntity labEntity = new LabEntity(labDto.getLabName(),
            labDto.getLabDescription(),
            labDto.getStackLocation());

        labRepository.save(labEntity);

        labEntity.setStackName(labEntity.getName() + "-" + labEntity.getId());

        labEntity.setUserGroupName(CreateStack.createUserGroup(labEntity));

        labRepository.save(labEntity);

        responseLabDto = new LabDto(
            labEntity.getId(),
            labEntity.getName(),
            labEntity.getLabDescription(),
            labEntity.getStackLocation(),
            labEntity.getStackName(),
            labEntity.getUserGroupName());
        return responseLabDto;
    }

    public List<LabDto> fetchAllLabs() {
        return labRepository.findAll()
            .stream()
            .map(
                labEntity -> new LabDto(
                    labEntity.getId(),
                    labEntity.getName(),
                    labEntity.getLabDescription(),
                    labEntity.getStackLocation(),
                    labEntity.getStackName(),
                    labEntity.getUserGroupName())
            ).collect(Collectors.toList());
    }

}
