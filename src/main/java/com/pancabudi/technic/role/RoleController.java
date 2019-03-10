package com.pancabudi.technic.role;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@Api(description = "Operation that handle role module", tags = "Roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    @ApiOperation(
            value = "Show list of roles"
    )
    public List<Role> index(){
        return roleService.findAll();
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping
    @ApiOperation(
            value = "Submit new role"
    )
    public void create(
            @ApiParam(value = "New role that will be submitted") @RequestBody Role role){
        roleService.create(role);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{id}")
    @ApiOperation(
            value = "Update a role"
    )
    public void update(
            @ApiParam(value = "Role's id that will be updated", example = "1") @PathVariable int id,
            @ApiParam(value = "Role that will be updated") @RequestBody Role role){
        roleService.update(id,role);
    }
}
