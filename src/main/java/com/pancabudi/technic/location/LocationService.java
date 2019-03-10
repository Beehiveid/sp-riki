package com.pancabudi.technic.location;

import com.pancabudi.technic.exception.ResourceNotFoundException;
import com.pancabudi.technic.person.Person;
import com.pancabudi.technic.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PersonService personService;

    public List<Location> findAll(){
        return locationRepository.findAll();
    }

    public void save(Location location) {
        locationRepository.save(location);
    }

    public Location findById(long id) {
        return locationRepository.findById(id).orElse(null);
    }

    public void update(long id, Location location) {
        Location loc = this.findById(id);

        if(loc != null){
            location.setId(id);
            this.save(location);
        }else{
            throw new ResourceNotFoundException("Location");
        }
    }

    void assignPerson(long id, List<Person> person) {
        Location location = this.findById(id);

        if(location == null)
            throw new ResourceNotFoundException("Location");

        person.forEach(
                p->{
                    Person existed = personService.findById(p.getId());

                    if(existed != null){
                        location.getPerson().add(existed);
                    }
                }
        );

        this.save(location);
    }

    void removePerson(long id, List<Person> person) {
        Location location = this.findById(id);

        if(location == null)
            throw new ResourceNotFoundException("Location");

        person.forEach(
                p->{
                    Person existed = personService.findById(p.getId());

                    if(existed != null){
                        location.getPerson().remove(existed);
                    }
                }
        );

        this.save(location);
    }
}
