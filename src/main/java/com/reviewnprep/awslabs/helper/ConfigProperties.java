package com.reviewnprep.awslabs.helper;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ConfigProperties {
    String stackName = "";
    String roleARN = "";
    String location = "";
    String key = "";
    String value = "";
}
