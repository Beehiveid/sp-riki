package com.beehive.riki.log;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/log")
@Api(description = "Operation that handle user's activities logging in system", tags = "Logs")
public class LogController {
    @Autowired
    private LogUserService logUserService;

    @GetMapping("/user")
    @ApiOperation(
            value = "Show all user's activities logs"
    )
    public List<LogUser> user(){
        return logUserService.findAll();
    }
}
