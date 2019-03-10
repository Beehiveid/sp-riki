package com.pancabudi.technic.machineType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/machine/type")
@Api(description = "Operation that handle type of machine module", tags = "Type of Machines")
public class MachineTypeController {
    @Autowired
    private MachineTypeService machineTypeService;

    @GetMapping
    @ApiOperation(
            value = "Show list of type of machines"
    )
    public List<MachineType> index(){
        return machineTypeService.findAll();
    }

    @GetMapping("/{id}")
    @ApiOperation(
            value = "Show type of machine by an id"
    )
    public MachineType findById(
            @ApiParam(value="Type of machine's id that will be displayed", example = "1") @PathVariable Long id){
        return machineTypeService.findById(id);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping
    @ApiOperation(
            value = "Submit new type of machine"
    )
    public void create(
            @ApiParam(value = "New type of machine that will be submitted") @RequestBody MachineType machineType){
        machineTypeService.create(machineType);
    }

    @GetMapping("/dataTables")
    @ApiOperation(
            value = "Show list of type of machines in DataTables format output"
    )
    public DataTablesOutput<MachineType> index(@Valid DataTablesInput input){
        return machineTypeService.findAll(input);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{id}")
    @ApiOperation(
            value = "Update type of machine"
    )
    public void update(
            @ApiParam(value = "Type of machine's id that will be updated", example = "1") @PathVariable Long id,
            @ApiParam(value = "Type of machine that will be updated")@RequestBody MachineType machineType){
        machineTypeService.update(id, machineType);
    }

    @PatchMapping("/copy/configuration")
    public void copyConfiguration(@RequestParam Long from, @RequestParam Long to){
        machineTypeService.copyConfiguration(from, to);
    }
}
