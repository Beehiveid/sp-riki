package com.beehive.riki.client;

import com.auth0.jwt.JWT;
import com.beehive.riki.log.LogUser;
import com.beehive.riki.log.LogUserService;
import com.beehive.riki.role.Role;
import com.beehive.riki.role.RoleService;
import com.beehive.riki.serviceRequestOrder.SROConsentFlow;
import com.beehive.riki.system.SystemEnvironment;
import com.beehive.riki.system.SystemEnvironmentServiceImpl;
import com.beehive.riki.users.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.beehive.riki.security.SecurityConstant.HEADER_STRING;
import static com.beehive.riki.security.SecurityConstant.TOKEN_PREFIX;

@Service
public class ClientService {
    @Autowired
    private ClientRepository  clientRepository;

    @Autowired
    private LogUserService logUserService;

    @Autowired
    private SystemEnvironmentServiceImpl systemEnvironmentService;

    @Autowired
    private RoleService roleService;

    public void submit(Client client){
        clientRepository.save(client);
    }

    public boolean verify(String cid, String sky, HttpServletRequest req){
        String xClient = req.getHeader("x-client-data");
        boolean valid = false;

        Client client = clientRepository.findByClientIdAndSecretKey(cid, sky);

        if(client != null){
            if(client.getRequestKey().equals(xClient)){
                valid = true;
                client.setLastAccess(new Date());
                this.submit(client);
            }
        }

        return valid;
    }

    public void logout(String cid, String sky) {
        Client client = clientRepository.findByClientIdAndSecretKey(cid, sky);
        clientRepository.delete(client);
    }

    public List<SessionInformation> findSessionInfo(long pid) {
        List<SessionInformation> sessionInformation = new ArrayList<>();
        List<Client> clients = clientRepository.findByPid(pid);

        clients.forEach(
                c->{
                    try {
                        sessionInformation.add(this.toSessionInformation(c));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        return sessionInformation;
    }

    private SessionInformation toSessionInformation(Client c) throws IOException {
        return new SessionInformation(c);
    }

    public void logout(long id, boolean all, HttpServletRequest request) {
        long pid = (long) request.getAttribute("pid");
        if(all){
            List<Client> clients = clientRepository.findByPid(pid);
            clientRepository.deleteInBatch(clients);
        }else{
            Client client = clientRepository.findById(id).orElse(null);
            assert client != null;

            try{
                if(client.getPid() == pid)
                    clientRepository.delete(client);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void logUser(HttpServletRequest req) {
        String token = req.getHeader(HEADER_STRING).replace(TOKEN_PREFIX,"");
        AppUser loggedUser = new AppUser();
        loggedUser.setId(JWT.decode(token).getClaim("uid").asInt());

        LogUser logUser = new LogUser();
        logUser.setLoggedUser(loggedUser);
        logUser.setEndpoint(req.getRequestURI());
        logUser.setMethod(req.getMethod());
        logUser.setSystemDate(new Date());
        logUserService.submit(logUser);
    }

    public List<String> defineSROScopes(AppUser a) {
        Role administrator = roleService.findByName("Administrator");
        SystemEnvironment sc = systemEnvironmentService.loadSROConfig();
        List<String> scopes = new ArrayList<>();

        try {
            SROConsentFlow[] sroConsentFlows = new ObjectMapper().readValue(sc.getValue(), SROConsentFlow[].class);

            for (SROConsentFlow flow : sroConsentFlows) {
                if(flow.getRoles().contains(a.getRole().getId()) || a.getRole().getId() == administrator.getId()){
                    scopes.add(flow.getFlow());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scopes;
    }

    public String defineUIConfig() {
        SystemEnvironment systemEnvironment = systemEnvironmentService.loadUIConfig();
        return systemEnvironment.getValue();
    }

    public String defineSROPriorityType() {
        SystemEnvironment systemEnvironment = systemEnvironmentService.loadSROPriorityConfig();
        return systemEnvironment.getValue();
    }
}
