package com.reviewnprep.awslabs.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private String status;
    private int status_code;
    private String status_message;
    private Object data;
}
