package com.pancabudi.technic.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogUserService {
    @Autowired
    private LogUserRepository logUserRepository;

    public void submit(LogUser logUser){
        logUserRepository.save(logUser);
    }

    public List<LogUser> findAll(){
        return logUserRepository.findAll();
    }
}
