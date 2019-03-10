package com.pancabudi.technic.person;

import com.pancabudi.technic.location.Location;
import com.pancabudi.technic.location.LocationService;
import com.pancabudi.technic.users.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/person")
@Api(description = "Operation that handle person (Employee) module", tags = "Person")
public class PersonController {
    @Autowired
    private PersonService personService;

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    @GetMapping
    @ApiOperation(
            value = "Show list of person"
    )
    public List<Person> index(){
        return personService.findAll();
    }

    @GetMapping("/{id}")
    @ApiOperation(
            value = "Show person by an id"
    )
    public Person findById(
            @ApiParam(value="Person's id that will be displayed", example = "1") @PathVariable long id){
        return personService.findById(id);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping
    @ApiOperation(
            value = "Submit new person"
    )
    public void create(
            @ApiParam(value="New person that will be submitted") @RequestBody Person person){
        personService.create(person);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{id}")
    @ApiOperation(
            value = "Update a person"
    )
    public void update(
            @ApiParam(value="Person's id that will be updated", example = "1")@PathVariable Long id,
            @ApiParam(value="Person that will be updated") @RequestBody Person person){
        personService.update(id,person);
    }

    @GetMapping("/{id}/location")
    public List<Location> getLocation(@PathVariable long id){
        return personService.getLocation(id);
    }

    @GetMapping("/leader")
    public Set<Person> getTeamLeaderFilter(@RequestParam Long location, @RequestParam(name = "filter", required = false) boolean filter){
        return personService.getTeamLeader(location, filter);
    }

    @GetMapping("/technician")
    public Set<Person> getTechnician(@RequestParam(name = "location", required = false) Long location, @RequestParam(name = "filter", required = false) boolean filter){
        if(location != null){
            return personService.getTechnician(location, filter);
        }else{
            return personService.getTechnician();
        }
    }
}