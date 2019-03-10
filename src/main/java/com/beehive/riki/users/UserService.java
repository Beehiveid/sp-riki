package com.beehive.riki.users;

import com.beehive.riki.email.EmailService;
import com.beehive.riki.role.Role;
import com.beehive.riki.role.RoleService;
import com.pancabudi.technic.email.EmailService;
import com.pancabudi.technic.exception.ResourceNotFoundException;
import com.pancabudi.technic.person.Person;
import com.pancabudi.technic.person.PersonService;
import com.pancabudi.technic.role.Role;
import com.pancabudi.technic.role.RoleService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PersonService personService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;

    public void create(AppUser user){
        userRepository.save(user);
    }

    public List<AppUser> findAdministrator() {
        Role role = roleService.findByName("Administrator");
        return userRepository.findByRoleId(role.getId());
    }

    DataTablesOutput<AppUser> findAll(DataTablesInput input){
        return userRepository.findAll(input);
    }

    @Deprecated
    void resetPassword(String email) {
        Person requester = personService.findByEmail(email);

        if(requester != null){
            List<AppUser> users = userRepository.findByPersonId(requester.getId());
            List<User> updated = new ArrayList<>();

            users.forEach(
                    u->{
                        String newPassword = RandomStringUtils.randomAlphanumeric(6);
                        updated.add(new User(u.getUsername(),newPassword, Collections.emptyList()));
                        u.setPassword(bCryptPasswordEncoder.encode(newPassword));
                    }
            );

            userRepository.saveAll(users);

            Context context = new Context();
            context.setVariable("email", email);
            context.setVariable("name", requester.getName());
            context.setVariable("users", updated);
            emailService.passwordReset(context);
        }
    }

    public List<AppUser> findTechnicians() {
        Role role = roleService.findByName("Teknisi");
        return userRepository.findByRoleId(role.getId());
    }

    void registration(AppUser appUser) {
        if(userRepository.existsByUsername(appUser.getUsername())){
            throw new RuntimeException("This username has been exist");
        }

        personService.create(appUser.getPerson());
        appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
        userRepository.save(appUser);
    }

    public void update(int id, AppUser appUser) {
        AppUser user = this.findById(id);

        if(user != null){
            appUser.setId(user.getId());
            userRepository.save(appUser);
            personService.update(user.getPerson().getId(), appUser.getPerson());
        }else{
            throw new ResourceNotFoundException("User");
        }
    }

    private AppUser findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    void resetPassword(AppUser appUser) {
        AppUser user = this.findById(appUser.getId());

        if(user != null){
            user.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
            userRepository.save(user);
        }else{
            throw new ResourceNotFoundException("User");
        }
    }

    public AppUser findByPerson(Person p) {
        List<AppUser> byPersonId = userRepository.findByPersonId(p.getId());
        return byPersonId.stream().findFirst().orElse(null);
    }

    public List<AppUser> findTeamLeader() {
        Role role = roleService.findByName("Kepala Regu");
        return userRepository.findByRoleId(role.getId());
    }
}
