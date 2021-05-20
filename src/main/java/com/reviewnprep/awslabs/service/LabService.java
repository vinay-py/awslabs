package com.reviewnprep.awslabs.service;

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

        responseLabDto = new LabDto(labEntity.getLabName(),
            labEntity.getLabDescription(),
            labEntity.getStackLocation());

        return responseLabDto;
    }

    public List<LabDto> fetchAllLabs() {
        return labRepository.findAll()
            .stream()
            .map(
                labEntity -> new LabDto(labEntity.getLabName(),
                    labEntity.getLabDescription(),
                    labEntity.getStackLocation())
            ).collect(Collectors.toList());
    }
}
