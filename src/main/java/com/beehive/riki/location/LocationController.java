package com.beehive.riki.location;

import com.beehive.riki.person.Person;
import com.pancabudi.technic.person.Person;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
@Api(description = "Operation that handle location module", tags = "Locations")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @GetMapping
    @ApiOperation(
            value = "Show list of locations"
    )
    public List<Location> index(){
        return locationService.findAll();
    }

    @PostMapping
    @ApiOperation(
            value="Submit new location"
    )
    public void create(
            @ApiParam(value = "New location to be entered") @RequestBody Location location){
        locationService.save(location);
    }

    @GetMapping("/{id}")
    @ApiOperation(
            value="Show location by an id"
    )
    public Location findById(
            @ApiParam(value = "Location's id that will be displayed", example = "1") @PathVariable long id){
        return locationService.findById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation(
            value="Update a location"
    )
    public void update(
            @ApiParam(value = "Location's id to be updated", example = "1") @PathVariable long id,
            @ApiParam(value = "Location to be updated")@RequestBody Location location){
        locationService.update(id, location);
    }

    @PatchMapping("/{id}/person")
    @ApiOperation(
            value="Update technician & group leader in location"
    )
    public void assignPerson(@PathVariable long id, @RequestBody List<Person> person){
        locationService.assignPerson(id, person);
    }

    @DeleteMapping("/{id}/person")
    @ApiOperation(
            value="Remove technician & group leader in location"
    )
    public void removePerson(@PathVariable long id, @RequestBody List<Person> person){
        locationService.removePerson(id, person);
    }
}
