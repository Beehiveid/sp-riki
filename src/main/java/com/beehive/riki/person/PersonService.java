package com.beehive.riki.person;

import com.beehive.riki.exception.ResourceNotFoundException;
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

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

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

    public Person findByName(String name) {
        return personRepository.findByName(name).orElse(null);
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
