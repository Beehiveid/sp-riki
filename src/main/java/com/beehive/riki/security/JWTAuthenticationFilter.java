package com.beehive.riki.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.beehive.riki.client.Client;
import com.beehive.riki.client.ClientInformation;
import com.beehive.riki.client.ClientService;
import com.beehive.riki.users.AppUser;
import com.beehive.riki.users.LoggedUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
    private AuthenticationManager authenticationManager;
    private ClientService clientService;

    JWTAuthenticationFilter(AuthenticationManager authenticationManager, ClientService clientService) {
        this.authenticationManager = authenticationManager;
        this.clientService = clientService;
        this.setFilterProcessesUrl("/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try{
            AppUser user = new ObjectMapper()
                    .readValue(req.getInputStream(), AppUser.class);

            if(req.getHeader("x-client-data") == null){
                throw new NullPointerException("x-client-data is null");
            }

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword(),
                            new ArrayList<>()
                    )
            );
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws JsonProcessingException {
        AppUser appUser = ((LoggedUser) auth.getPrincipal()).getUser();

        UUID clientId = UUID.randomUUID();
        UUID secretKey = UUID.randomUUID();

        String ua = req.getHeader("user-agent");
        String ciString = null;

        if(ua != null){
            ReadableUserAgent meta = parser.parse(ua);

            ClientInformation clientInformation = new ClientInformation(meta);
            ciString = new ObjectMapper().writeValueAsString(clientInformation);
        }

        List<String> sroScopes = clientService.defineSROScopes(appUser);
        String uiConfig = clientService.defineUIConfig();
        String spt = clientService.defineSROPriorityType();
        String sroScopesStr = String.join(",", sroScopes);

        String token = JWT.create()
                .withSubject(((User)auth.getPrincipal()).getUsername())
                .withClaim("uid", appUser.getId())
                .withClaim("pid", appUser.getPerson() == null?null:appUser.getPerson().getId())
                .withClaim("name", appUser.getPerson() == null?null:appUser.getPerson().getName())
                .withClaim("cid", clientId.toString())
                .withClaim("sky", secretKey.toString())
                .withClaim("scope", appUser.getRole().getName().toUpperCase().replace(" ","_"))
                .withClaim("sro", sroScopesStr)
                .withClaim("uic", uiConfig)
                .withClaim("spt", spt)
                .sign(Algorithm.HMAC512(SecurityConstant.SECRET.getBytes()));

        String xClient = req.getHeader("x-client-data");

        Client client = new Client();
        client.setClientId(String.valueOf(clientId));
        client.setSecretKey(String.valueOf(secretKey));
        client.setInformation(ciString);
        client.setRequestKey(xClient);
        client.setLocation(ua);
        client.setLastAccess(new Date());
        client.setPid(appUser.getPerson().getId());
        clientService.submit(client);

        res.addHeader(SecurityConstant.HEADER_STRING, SecurityConstant.TOKEN_PREFIX + token);
    }


}
