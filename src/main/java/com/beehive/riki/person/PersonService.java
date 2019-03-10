package com.beehive.riki.person;

import com.beehive.riki.exception.ResourceNotFoundException;
import com.beehive.riki.location.Location;
import com.beehive.riki.location.LocationService;
import com.beehive.riki.role.RoleService;
import com.beehive.riki.users.AppUser;
import com.beehive.riki.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Value("${super.name}")
    private String ADMINISTRATOR_NAME;

    public void create(Person person){
        personRepository.save(person);
    }

    public List<Person> findAll(){
        return personRepository.findAll();
    }

    public Person findById(long id){
        return personRepository.findById(id).orElse(null);
    }

    public boolean isAdministratorExist(){
        return personRepository.existsByName(ADMINISTRATOR_NAME);
    }

    public void update(Long id, Person person) {
        Person p = this.findById(id);

        if(p != null){
            person.setId(p.getId());
            personRepository.save(person);
        }else{
            throw new ResourceNotFoundException("This person");
        }
    }

    public Person findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    public Person getLogged(){
        long pid = (long) request.getAttribute("pid");
        return this.findById(pid);
    }

    List<Location> getLocation(long id) {
        Person person = this.findById(id);

        if(person == null)
            throw new ResourceNotFoundException("Person");

        List<Location> collect = locationService.findAll().stream().filter(l -> l.getPerson().contains(person)).collect(Collectors.toList());

        if(collect.size() < 1)
            collect = locationService.findAll();

        return collect;
    }

    public List<Location> getLocation(){
        return this.getLocation(this.getLogged().getId());
    }

    public Person findByName(String name) {
        return personRepository.findByName(name).orElse(null);
    }

    Set<Person> getTeamLeader(long location, boolean filter) {
        Location existing = locationService.findById(location);

        if(existing == null)
            throw new ResourceNotFoundException("This location");

        return this.collectPeople(userService.findTeamLeader(), existing.getPerson(), filter);
    }

    Set<Person> getTechnician(long location, boolean filter) {
        Location existing = locationService.findById(location);

        if(existing == null)
            throw new ResourceNotFoundException("This location");

        return this.collectPeople(userService.findTechnicians(), existing.getPerson(), filter);
    }

    private Set<Person> collectPeople(List<AppUser> users, Set<Person> person, boolean filter) {
        Set<Person> collected = new HashSet<>();
        if(!filter){
            for (AppUser user : users) {
                boolean exist = person.contains(user.getPerson());

                if(exist)
                    collected.add(user.getPerson());
            }
        }else{
            for (AppUser user : users) {
                boolean exist = person.contains(user.getPerson());

                if(!exist)
                    collected.add(user.getPerson());
            }
        }

        return collected;
    }

    Set<Person> getTechnician() {
        Set<Person> technicians = new HashSet<>();
        userService.findTechnicians().forEach(
                u->{
                    technicians.add(u.getPerson());
                }
        );
        return technicians;
    }
}
