package com.reviewnprep.awslabs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewnprep.awslabs.dto.LabDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
public class AWSLabsApplicationIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private LabDto labDto;
    private String stackLocation = "https://cf-templates-pfqrnj6qio4k-us-east-2.s3.us-east-2.amazonaws.com/templates/CreateMFALabGroup.json";

    @BeforeEach
    public void setup(){
        labDto = new LabDto("MFALab", "Set up MFA in AWS IAM", stackLocation);
    }

    @Test
    public void createLab() throws Exception {
        mockMvc
            .perform(
                post("/lab")
                    .content(objectMapper.writeValueAsString(labDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.getReasonPhrase()))
            .andExpect(jsonPath("$.status_code").value(HttpStatus.CREATED.value()))
            .andExpect(jsonPath("$.status_message").value("Lab Created Successfully!"))
            .andExpect(jsonPath("$.data.labName").value("MFALab"))
            .andDo(
                document(
                    "Create-Lab",
                    requestFields(
                        fieldWithPath("labName").description("Lab Name"),
                        fieldWithPath("labDescription").description("Lab Description"),
                        fieldWithPath("stackLocation").description("S3 Location for the Stack")
                    ),
                    responseFields(
                        fieldWithPath("status").description("Return the http status description"),
                        fieldWithPath("status_code").description("Return the http status code"),
                        fieldWithPath("status_message").description("Return Lab creation status message"),
                        fieldWithPath("data").description("Return the created Lab Object"),
                        fieldWithPath("data.labName").description("Lab Name"),
                        fieldWithPath("data.labDescription").description("Lab Description"),
                        fieldWithPath("data.stackLocation").description("S3 Location for the Stack")
                    )
                )
            );
    }

    @Test
    public void getZeroLab() throws Exception{
        mockMvc
            .perform(
                get("/lab"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.status_code").value(200))
            .andExpect(jsonPath("$.data.length()").value(0))
            .andDo(
                document(
                    "Get-Zero-Labs",
                    responseFields(
                        fieldWithPath("status").description("Return the http status description"),
                        fieldWithPath("status_code").description("Return the http status code"),
                        fieldWithPath("status_message").description("status message"),
                        fieldWithPath("data").description("List of Labs"))));
    }
}
