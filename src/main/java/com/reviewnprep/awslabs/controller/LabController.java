package com.reviewnprep.awslabs.controller;

import com.reviewnprep.awslabs.dto.LabDto;
import com.reviewnprep.awslabs.helper.Response;
import com.reviewnprep.awslabs.service.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("lab")
public class LabController {
    @Autowired
    LabService labService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Object createLab(@RequestBody LabDto labDto){

        LabDto responseLabDto =this.labService.createLab(labDto);
        Response response;
        if (responseLabDto == null) {
            response =
                new Response(
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Lab Creation Not Successful!",
                    null);
        } else {
            response =
                new Response(
                    HttpStatus.CREATED.getReasonPhrase(),
                    HttpStatus.CREATED.value(),
                    "Lab Created Successfully!",
                    responseLabDto);
        }
        return response;
    }

    @GetMapping
    public Response getAllLabs (){
        List<LabDto> labDtoList = labService.fetchAllLabs();
        return new Response(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(), "List of Active Labs", labDtoList);
    }
}
