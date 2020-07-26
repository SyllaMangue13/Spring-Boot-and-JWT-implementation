package io.javabrains.springsecurityjwt.services;

import io.javabrains.springsecurityjwt.JwtUtil;
import io.javabrains.springsecurityjwt.MyUserDetailsService;
import io.javabrains.springsecurityjwt.models.AuthentificationRequest;
import io.javabrains.springsecurityjwt.models.AuthentificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class helloResource {
    @Autowired
    private AuthenticationManager authenticationManager ;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @RequestMapping( "/hello" )
        public String hello() {
        return  "hello world 13" ;
    }

    @RequestMapping( value = "/authenticate" , method = RequestMethod.POST)
     public ResponseEntity<?> createAuthentificationToken(@RequestBody AuthentificationRequest authentificationRequest) throws  Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authentificationRequest.getUsername(), authentificationRequest.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authentificationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthentificationResponse(jwt));

    }
}
