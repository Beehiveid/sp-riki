package com.beehive.riki.common;

import com.beehive.riki.role.Role;
import com.beehive.riki.role.RoleService;
import com.beehive.riki.serviceRequestOrder.ServiceRequestOrderService;
import com.beehive.riki.users.AppUser;
import com.beehive.riki.users.UserService;
import com.pancabudi.technic.complaintType.ComplaintType;
import com.pancabudi.technic.complaintType.ComplaintTypeService;
import com.pancabudi.technic.person.Person;
import com.pancabudi.technic.person.PersonService;
import com.pancabudi.technic.role.Role;
import com.pancabudi.technic.role.RoleService;
import com.pancabudi.technic.serviceRequestOrder.ServiceRequestOrderService;
import com.pancabudi.technic.system.SystemEnvironment;
import com.pancabudi.technic.system.SystemEnvironmentServiceImpl;
import com.pancabudi.technic.users.AppUser;
import com.pancabudi.technic.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pancabudi.technic.common.SystemConstant.ADMINISTRATOR_ROLE;
import static com.pancabudi.technic.common.SystemConstant.REPORTING_TOKEN;
import static com.pancabudi.technic.common.SystemConstant.SRO_PRIORITY_TOKEN;

@Component
public class Initiator {
    private static final Logger log = LoggerFactory.getLogger(Initiator.class);
    private static final SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

    @Autowired
    private RoleService roleService;

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @Autowired
    private PersonService personService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private SystemEnvironmentServiceImpl systemEnvironmentService;

    @Autowired
    private ServiceRequestOrderService serviceRequestOrderService;

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

    @PostConstruct
    public void complaintTypeInit(){
        String name = "Complaint Type Initiation";
        log.info("[{}] executed at {}",name , formatter.format(new Date()));

        List<String> types = new ArrayList<>();
        types.add(SystemConstant.AUTO_ROUTING_MAINTENANCE);

        for(String type : types){
            boolean exist = complaintTypeService.isExist(type);

            if(!exist){
                complaintTypeService.create(new ComplaintType(type));
                log.info(type+" complaint type was created");
            }
        }
    }

    @PostConstruct
    public void sroConfigInit(){
        String name = "SRO Configuration Initiation";
        log.info("[{}] executed at {}",name , formatter.format(new Date()));

        SystemEnvironment UIConfig = systemEnvironmentService.loadUIConfig();

        if(UIConfig == null)
            serviceRequestOrderService.postConsent(sroFlow);

        SystemEnvironment reportingConfig = systemEnvironmentService.loadReportingConfig();

        if(reportingConfig == null){
            reportingConfig = new SystemEnvironment();
            reportingConfig.setValue(sroReports);
            reportingConfig.setSystemDate(new Date());
            reportingConfig.setToken(REPORTING_TOKEN);

            systemEnvironmentService.save(reportingConfig);
        }

        SystemEnvironment sroPriorityConfig = systemEnvironmentService.loadSROPriorityConfig();

        if(sroPriorityConfig == null){
            sroPriorityConfig = new SystemEnvironment();
            sroPriorityConfig.setValue(sroPriority);
            sroPriorityConfig.setSystemDate(new Date());
            sroPriorityConfig.setToken(SRO_PRIORITY_TOKEN);

            systemEnvironmentService.save(sroPriorityConfig);
        }
    }
}
