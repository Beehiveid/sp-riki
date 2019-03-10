package com.pancabudi.technic.complaintType;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/complaint/type")
@Api(description = "Operation that handle type of complaint module", tags = "Type of Complaints")
public class ComplaintTypeController {
    @Autowired
    private ComplaintTypeService complaintTypeService;

    @GetMapping
    @ApiOperation(
            value = "Show all type of complaints")
    public List<ComplaintType> findAll(){
        return complaintTypeService.findAll(true);
    }

    @GetMapping("/{id}")
    @ApiOperation(
            value = "Show type of complaint by an id",
            notes="Pass an id to this endpoint and web service will return a related type of complaint object")
    public ComplaintType findById(
            @ApiParam(value="Type of complaint's id that will be displayed", example = "1") @PathVariable Long id){
        return complaintTypeService.findById(id);
    }

    @GetMapping("/dataTables")
    @ApiOperation(
            value = "Show all type of complaints list in DataTables format output")
    public DataTablesOutput<ComplaintType> index(@Valid DataTablesInput input){
        return complaintTypeService.findAll(input);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping
    @ApiOperation(value = "Submit new type of complaint")
    public void create(
            @ApiParam(value = "New type of complaint to be entered") @RequestBody ComplaintType complaintType){
        complaintTypeService.create(complaintType);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{id}")
    @ApiOperation(value = "Update a type of complaint")
    public void update(
            @ApiParam(value = "Type of complaint's id to be updated", example = "1") @PathVariable Long id,
            @ApiParam(value = "Type of complaint to be updated") @RequestBody ComplaintType complaintType){
        complaintTypeService.update(id, complaintType);
    }
}
