package com.pancabudi.technic.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.pancabudi.technic.client.ClientService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.pancabudi.technic.security.SecurityConstant.SECRET;
import static com.pancabudi.technic.security.SecurityConstant.TOKEN_PREFIX;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private ClientService clientService;

    JWTAuthorizationFilter(AuthenticationManager authenticationManager, ClientService clientService) {
        super(authenticationManager);
        this.clientService = clientService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String header = req.getHeader(SecurityConstant.HEADER_STRING);
        String xClient = req.getHeader("x-client-data");
        if(xClient == null){
            chain.doFilter(req, res);
            return;
        }

        if(header == null || !header.startsWith(TOKEN_PREFIX)){
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req){
        String token = req.getHeader(SecurityConstant.HEADER_STRING);
        if(token != null){
            String pureToken = token.replace(TOKEN_PREFIX,"");
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(pureToken)
                    .getSubject();

            if(user != null){
                long pid = JWT.decode(pureToken).getClaim("pid").asLong();
                req.setAttribute("pid",pid);

                clientService.logUser(req);

                String cid = JWT.decode(pureToken).getClaim("cid").asString();
                String sky = JWT.decode(pureToken).getClaim("sky").asString();
                boolean valid = clientService.verify(cid, sky, req);

                if(!valid){
                    return null;
                }

                Set<GrantedAuthority> authorities = new HashSet<>();
                Claim scope = JWT.decode(token.replace(TOKEN_PREFIX,"")).getClaim("scope");
                authorities.add(new SimpleGrantedAuthority("ROLE_"+scope.asString()));

                req.setAttribute("scope", scope.asString());

                return new UsernamePasswordAuthenticationToken(user, null, authorities);
            }
            return null;
        }
        return null;
    }
}
