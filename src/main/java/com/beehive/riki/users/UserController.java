package com.beehive.riki.users;

import com.auth0.jwt.JWT;
import com.beehive.riki.client.ClientService;
import com.beehive.riki.client.SessionInformation;
import com.beehive.riki.person.Person;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.beehive.riki.security.SecurityConstant.HEADER_STRING;
import static com.beehive.riki.security.SecurityConstant.TOKEN_PREFIX;

@RestController
@RequestMapping("/user")
@Api(description = "Operation that handle users module", tags = "Users")
public class UserController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping
    @ApiOperation(
            value = "Submit new user"
    )
    public void create(
            @ApiParam(value = "New user that will be submitted") @RequestBody AppUser user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.create(user);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/dataTables")
    @ApiOperation(
            value = "Show list of users in DataTables format output"
    )
    public DataTablesOutput<AppUser> index(@Valid DataTablesInput input){
        return userService.findAll(input);
    }

    @PostMapping("/logout")
    @ApiOperation(
            value = "Log out a user"
    )
    public void logout(HttpServletRequest request){
        String token = request.getHeader(HEADER_STRING).replace(TOKEN_PREFIX, "");
        String cid = JWT.decode(token).getClaim("cid").asString();
        String sky = JWT.decode(token).getClaim("sky").asString();

        clientService.logout(cid, sky);
    }

    @DeleteMapping("/session")
    @ApiOperation(
            value = "Clear user's sessions"
    )
    public void sessionClear(
            @ApiParam(
                    value = "Session's id that will be cleared." +
                    "If empty, system will clear all of user's active sessions",
                    example = "1") @RequestParam(name = "id", required = false, defaultValue = "-1")String id,
            HttpServletRequest request){
        boolean flag = false;
        long sid = Long.parseLong(id);

        if(sid < 0){
            flag = true;
        }

        clientService.logout(sid, flag, request);
    }

    @GetMapping("/session")
    @ApiOperation(
            value = "Show list of user's sessions"
    )
    public List<SessionInformation> session(HttpServletRequest request){
        long pid = (long) request.getAttribute("pid");
        return clientService.findSessionInfo(pid);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/reset")
    @ApiOperation(
            value = "Reset user's password by email"
    )
    public void reset(
            @ApiParam(
                    value = "User's email to send brand new passwords, system only " +
                            "recognize the email rather than a whole Person object") @RequestBody Person person){
        userService.resetPassword(person.getEmail());
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PatchMapping("/password/reset")
    @ApiOperation(
            value = "Reset user's password by Administrator"
    )
    public void reset(
            @ApiParam(value = "User that will be reset") @RequestBody AppUser appUser){
        userService.resetPassword(appUser);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/registration")
    @ApiOperation(
            value = "Register new user"
    )
    public void registration(
            @ApiParam(value = "User that will be registered") @RequestBody AppUser appUser){
        userService.registration(appUser);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{id}")
    @ApiOperation(
            value = "Update a user"
    )
    public void update(
            @ApiParam(value = "User's id that will be updated", example = "1") @PathVariable int id,
            @ApiParam(value = "User that will be updated") @RequestBody AppUser appUser){
        userService.update(id, appUser);
    }
}