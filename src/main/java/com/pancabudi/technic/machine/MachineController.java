package com.pancabudi.technic.machine;

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
@RequestMapping("/machine")
@Api(description = "Operation that handle machines module", tags = "Machines")
public class MachineController {
    @Autowired
    private MachineService machineService;

    @GetMapping
    @ApiOperation(
            value = "Show list of machines"
    )
    public List<Machine> index(){
        return machineService.findAll();
    }

    @GetMapping("/information")
    @ApiOperation(
            value = "Show all machine's information",
            notes = "More information related to machines included a number of outstanding SRO"
    )
    public List<MachineInformation> getMachinesInformation(){
        return machineService.getMachinesInformation();
    }

    @GetMapping("/{id}")
    @ApiOperation(
            value = "Show machine by an id"
    )
    public Machine findById(
            @ApiParam(value="Location's id that will be displayed", example = "1") @PathVariable Long id){
        return machineService.findById(id);
    }

    @GetMapping("/{id}/information")
    @ApiOperation(
            value = "Show machine's information by an id"
    )
    public MachineInformation getMachineInformation(
            @ApiParam(value="Location's id that will be displayed", example = "1") @PathVariable Long id){
        return machineService.getMachinesInformation(id);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping
    @ApiOperation(
            value = "Submit new machine"
    )
    public void create(
            @ApiParam(value = "New machine to be entered") @RequestBody Machine machine){
        machineService.create(machine);
    }

    @GetMapping("/dataTables")
    @ApiOperation(
            value = "Show list of machines in DataTables format output"
    )
    public DataTablesOutput<Machine> index(@Valid DataTablesInput input){
        return machineService.findAll(input);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{id}")
    @ApiOperation(
            value = "Update a machine"
    )
    public void update(
            @ApiParam(value = "Machine's id to be updated", example = "1") @PathVariable Long id,
            @ApiParam(value = "Machine to be updated") @RequestBody Machine machine){
        machineService.update(id, machine);
    }

    @GetMapping("/{id}/handling")
    @ApiOperation(
            value = "Show machine's handling histories"
    )
    public List<MachineHistory> machineHistory(
            @ApiParam(value = "Machine's id to be displayed", example = "1") @PathVariable long id){
        return machineService.history(id);
    }

    @Deprecated
    @PostMapping("/bulk")
    public void bulk(){
        // machineService.bulk();
    }
}
