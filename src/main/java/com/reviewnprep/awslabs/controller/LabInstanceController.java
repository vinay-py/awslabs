package com.reviewnprep.awslabs.controller;

import com.reviewnprep.awslabs.dto.LabInstanceDto;
import com.reviewnprep.awslabs.helper.Response;
import com.reviewnprep.awslabs.service.LabInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("labInstance")
public class LabInstanceController {

    @Autowired
    LabInstanceService labInstanceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response createLabInstance (@RequestBody LabInstanceDto labInstanceDto){
        LabInstanceDto responseLabInstanceDto = this.labInstanceService.createLabInstance(labInstanceDto);

        Response response;

        if (responseLabInstanceDto == null) {
            response =
                new Response(
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Invoice Creation Not Successful!",
                    null);
        } else {
            response =
                new Response(
                    HttpStatus.CREATED.getReasonPhrase(),
                    HttpStatus.CREATED.value(),
                    "Lab Instance Created Successfully!",
                    responseLabInstanceDto);
        }

        return response;
    }

    @GetMapping
    public Response getAllLabs (){
        List<LabInstanceDto> labInstances = labInstanceService.fetchAllLabs();
        return new Response(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(), "List of All Lab Instances", labInstances);
    }
}
