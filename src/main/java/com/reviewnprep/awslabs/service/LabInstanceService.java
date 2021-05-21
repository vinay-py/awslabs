package com.reviewnprep.awslabs.service;

import com.reviewnprep.awslabs.awsall.CreateStack;
import com.reviewnprep.awslabs.dto.LabInstanceDto;
import com.reviewnprep.awslabs.entity.LabEntity;
import com.reviewnprep.awslabs.entity.LabInstanceEntity;
import com.reviewnprep.awslabs.repository.LabInstanceRepository;
import com.reviewnprep.awslabs.repository.LabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.*;
import software.amazon.awssdk.services.cloudformation.waiters.CloudFormationWaiter;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
import software.amazon.awssdk.services.sts.model.Credentials;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class LabInstanceService {

    @Autowired
    LabInstanceRepository labInstanceRepository;
    @Autowired
    LabRepository labRepository;


    public LabInstanceDto createLabInstance(LabInstanceDto labInstanceDto) {
        LabInstanceDto newLabInstanceDto = null;
        LabEntity labEntity = labRepository.findByName(labInstanceDto.getLabName());

        if (labEntity != null) {
            LabInstanceEntity labInstanceEntity = new LabInstanceEntity(labEntity, labInstanceDto.getDurationInSeconds());

            if (labInstanceDto.getCreateDate() != null) {
                labInstanceEntity.setCreateDate(
                    labInstanceEntity.getCreateDate()
                );
            }

            labInstanceRepository.save(labInstanceEntity);

            labInstanceEntity.setStackName(labInstanceEntity.getLab().getStackName() + "-" + labInstanceEntity.getId());

            labInstanceEntity.setUserName(CreateStack.createUser(labInstanceEntity));

            newLabInstanceDto = new LabInstanceDto(
                labInstanceEntity.getId(),
                labInstanceEntity.getLab().getName(),
                labInstanceEntity.getStackName(),
                labInstanceEntity.getUserName(),
                labInstanceEntity.getDurationInSeconds(),
                labInstanceEntity.getCreateDate(),
                labInstanceEntity.getCreateTime(),
                labInstanceEntity.isActive());
        }

        return newLabInstanceDto;
    }

    public List<LabInstanceDto> fetchAllLabs() {
        return labInstanceRepository
            .findAll()
            .stream()
            .map(
                labInstanceEntity -> new LabInstanceDto(
                    labInstanceEntity.getId(),
                    labInstanceEntity.getLab().getName(),
                    labInstanceEntity.getStackName(),
                    labInstanceEntity.getUserName(),
                    labInstanceEntity.getDurationInSeconds(),
                    labInstanceEntity.getCreateDate(),
                    labInstanceEntity.getCreateTime(),
                    labInstanceEntity.isActive()
                )
            )
            .collect(Collectors.toList());
    }
}
