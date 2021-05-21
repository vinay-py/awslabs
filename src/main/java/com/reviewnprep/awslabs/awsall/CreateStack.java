package com.reviewnprep.awslabs.awsall;

import com.reviewnprep.awslabs.entity.LabEntity;
import com.reviewnprep.awslabs.entity.LabInstanceEntity;
import com.reviewnprep.awslabs.service.LabService;
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
import java.util.Properties;


public class CreateStack {
//    private static CloudFormationClient cloudFormationClient;
    private static StaticCredentialsProvider awsCredentialsProvider;

    private static String stackName = "";
    private static String roleARN = "";
    private static String location = "";
    private static String key = "";
    private static String value = "";

    public static void createCredentials () {
        try (InputStream input = LabService.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties properties = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }

            properties.load(input);

            stackName = properties.getProperty("stackName");
            roleARN = properties.getProperty("roleARN");
            location = properties.getProperty("location");
            key = properties.getProperty("key");
            value = properties.getProperty("value");

        } catch (IOException e) {
            e.printStackTrace();
        }

        String roleSessionName = "Session_1";

        StsClient stsClient = StsClient.builder()
            .region(Region.US_EAST_2)
            .build();

        AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder()
            .roleArn(roleARN)
            .roleSessionName(roleSessionName)
            .durationSeconds(900)
            .build();

        AssumeRoleResponse assumeRoleResponse = stsClient.assumeRole(assumeRoleRequest);

        Credentials credentials = assumeRoleResponse.credentials();

//        System.out.println("Access Key ID : " + credentials.accessKeyId());
//        System.out.println("Secret Access Key : " + credentials.secretAccessKey());
//        System.out.println("Session Token : " + credentials.sessionToken());

        AwsCredentials awsCredentials = AwsSessionCredentials.create(credentials.accessKeyId(), credentials.secretAccessKey(), credentials.sessionToken());
        awsCredentialsProvider = StaticCredentialsProvider.create(awsCredentials);
    }

   public static String createUserGroup(LabEntity labEntity) {

       CreateStack.createCredentials();
       CloudFormationClient cloudFormationClient = CloudFormationClient.builder()
           .region(Region.US_EAST_2)
           .credentialsProvider(awsCredentialsProvider)
           .build();

        try{
            CloudFormationWaiter waiter = cloudFormationClient.waiter();

            Parameter paramUserName = Parameter.builder()
                .parameterKey("paramUserGroupName")
                .parameterValue(labEntity.getName() + labEntity.getId())
                .build();

            CreateStackRequest stackRequest = CreateStackRequest.builder()
                .stackName(labEntity.getStackName())
                .templateURL(labEntity.getStackLocation())
                .roleARN(roleARN)
                .parameters(paramUserName)
                .onFailure(OnFailure.ROLLBACK)
                .capabilities(Capability.CAPABILITY_NAMED_IAM)
                .build();

            cloudFormationClient.createStack(stackRequest);
            DescribeStacksRequest stacksRequest = DescribeStacksRequest.builder()
                .stackName(labEntity.getStackName())
                .build();

            WaiterResponse<DescribeStacksResponse> waiterResponse = waiter.waitUntilStackCreateComplete(stacksRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);
            System.out.println(stacksRequest.stackName() + " is ready");

        } catch (CloudFormationException exception){
            System.err.println(exception.getMessage());
            System.exit(1);
        }

       ListStackResourcesResponse listStackGroups = cloudFormationClient.listStackResources(ListStackResourcesRequest.builder()
           .stackName(labEntity.getStackName())
           .build());

       listStackGroups.stackResourceSummaries()
           .forEach(stackResourceSummary -> {
               System.out.println("The logicalResourceId : " + stackResourceSummary.logicalResourceId());
               System.out.println("The physicalResourceId : " + stackResourceSummary.physicalResourceId());
               System.out.println("The resourceType : " + stackResourceSummary.resourceType());
               System.out.println("The resourceStatus : " + stackResourceSummary.resourceStatus());
           });

       cloudFormationClient.close();

       return listStackGroups.stackResourceSummaries().get(0).physicalResourceId();

    }

    public static String createUser(LabInstanceEntity labInstanceEntity) {
        CreateStack.createCredentials();
        CloudFormationClient cloudFormationClient = CloudFormationClient.builder()
            .region(Region.US_EAST_2)
            .credentialsProvider(awsCredentialsProvider)
            .build();

        try{
            CloudFormationWaiter waiter = cloudFormationClient.waiter();
            Parameter paramUserName = Parameter.builder()
                .parameterKey("paramUserName")
                .parameterValue("reviewnprep-" + labInstanceEntity.getLab().getUserGroupName() + labInstanceEntity.getId())
                .build();

            Parameter paramUserPassword = Parameter.builder()
                .parameterKey("paramUserPassword")
                .parameterValue("Training123")
                .build();

            Parameter paramGroups = Parameter.builder()
                .parameterKey("paramGroups")
                .parameterValue(labInstanceEntity.getLab().getUserGroupName())
                .build();

            CreateStackRequest stackRequest = CreateStackRequest.builder()
                .stackName(labInstanceEntity.getStackName())
                .templateURL("https://vinaypy-cf-templates.s3.us-east-2.amazonaws.com/CreateUser.json")
                .roleARN(roleARN)
                .onFailure(OnFailure.ROLLBACK)
                .parameters(paramUserName,paramUserPassword,paramGroups)
                .capabilities(Capability.CAPABILITY_NAMED_IAM)
                .build();

            cloudFormationClient.createStack(stackRequest);
            DescribeStacksRequest stacksRequest = DescribeStacksRequest.builder()
                .stackName(labInstanceEntity.getStackName())
                .build();

            WaiterResponse<DescribeStacksResponse> waiterResponse = waiter.waitUntilStackCreateComplete(stacksRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);
            System.out.println(stacksRequest.stackName() + " is ready");

        } catch (CloudFormationException exception){
            System.err.println(exception.getMessage());
            System.exit(1);
        }
        ListStackResourcesResponse listStackGroups = cloudFormationClient.listStackResources(ListStackResourcesRequest.builder()
            .stackName(labInstanceEntity.getStackName())
            .build());

        listStackGroups.stackResourceSummaries()
            .forEach(stackResourceSummary -> {
                System.out.println("The logicalResourceId : " + stackResourceSummary.logicalResourceId());
                System.out.println("The physicalResourceId : " + stackResourceSummary.physicalResourceId());
                System.out.println("The resourceType : " + stackResourceSummary.resourceType());
                System.out.println("The resourceStatus : " + stackResourceSummary.resourceStatus());
            });

        cloudFormationClient.close();

        return listStackGroups.stackResourceSummaries().get(0).physicalResourceId();
    }

}
