package com.beehive.riki.solutionType;

import com.beehive.riki.common.ControllerFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/solution/type")
@Api(description = "Operation that handle type of solution module", tags = "Type of Solutions")
public class SolutionTypeController implements ControllerFactory<SolutionType, Long> {
    @Autowired
    private SolutionTypeService solutionTypeService;

    @Override
    @GetMapping
    @ApiOperation(
            value = "Show list of type of solutions"
    )
    public List<SolutionType> index() {
        return solutionTypeService.findAll();
    }

    @Override
    @GetMapping("/{id}")
    @ApiOperation(
            value = "Show a type of solution by an id"
    )
    public SolutionType findById(
            @ApiParam(value = "Type of solution's id that will be displayed", example = "1") @PathVariable Long id) {
        return solutionTypeService.findById(id);
    }

    @Override
    @PostMapping
    @ApiOperation(
            value = "Submit new type of solution"
    )
    public void create(
            @ApiParam(value = "New type of solution that will be submitted") @RequestBody SolutionType solutionType) {
        solutionTypeService.save(solutionType);
    }

    @GetMapping("/dataTables")
    @ApiOperation(
            value = "Show list of type of solutions in DataTables format output"
    )
    public DataTablesOutput<SolutionType> dataTables(@Valid DataTablesInput input){
        return solutionTypeService.findAll(input);
    }
}