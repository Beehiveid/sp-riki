package com.beehive.riki.common;

import com.beehive.riki.person.Person;
import com.beehive.riki.person.PersonService;
import com.beehive.riki.role.Role;
import com.beehive.riki.role.RoleService;
import com.beehive.riki.system.SystemEnvironmentServiceImpl;
import com.beehive.riki.users.AppUser;
import com.beehive.riki.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.beehive.riki.common.SystemConstant.ADMINISTRATOR_ROLE;

@Component
public class Initiator {
    private static final Logger log = LoggerFactory.getLogger(Initiator.class);
    private static final SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

    @Autowired
    private RoleService roleService;

    @Autowired
    private PersonService personService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private SystemEnvironmentServiceImpl systemEnvironmentService;

    @Value("${super.username}")
    private String username;

    @Value("${super.password}")
    private String password;

    @Value("${super.email}")
    private String email;

    @Value("${super.name}")
    private String name;

    @Value("${sro.flow}")
    private String sroFlow;

    @Value("${sro.reports}")
    private String sroReports;

    @Value("${sro.priority}")
    private String sroPriority;

    @PostConstruct
    public void roleInit(){
        String name = "Role Initiation";
        log.info("[{}] executed at {}",name , formatter.format(new Date()));
        String roles[] = new String[]{"Administrator","Kepala Regu","Teknisi"};

        int i = 0;
        for(String role : roles){
            boolean exist = roleService.existByName(role);

            if(!exist){
                roleService.create(new Role(i, role));
                log.info(role+" role was created");
            }

            if(role.equals(ADMINISTRATOR_ROLE)){
                this.defineAdministrator();
            }
            i++;
        }
    }

    private void defineAdministrator() {
        boolean isOkay = this.isDefaultAdministratorOkay();

        if(!isOkay){
            Person administrator = new Person();
            if(!personService.isAdministratorExist()){
                administrator.setName(this.name);
                administrator.setEmail(this.email);
                personService.create(administrator);
            }else{
                administrator = personService.findByName(this.name);
            }

            AppUser exist = userService.findByPerson(administrator);
            if(exist == null){
                AppUser user = new AppUser();
                user.setPerson(administrator);
                user.setUsername(administrator.getName().toLowerCase());
                user.setPassword(bCryptPasswordEncoder.encode(this.password));

                Role role = roleService.findByName(ADMINISTRATOR_ROLE);
                user.setRole(role);

                userService.create(user);
                log.info("Administrator account was created automatically by System. Contact "+administrator.getEmail().toLowerCase()+" for more information.");
            }else{
                log.info("Default administrator account is good and sound");
            }
        }else{
            log.info("Default administrator account is good and sound");
        }
    }

    private boolean isDefaultAdministratorOkay() {
        Person person = personService.findByName(this.name);

        if(person == null)
            return false;

        AppUser user = userService.findByPerson(person);
        return user != null;
    }
}
