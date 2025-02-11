package com.algostyle.authentication.rest;


import com.algostyle.authentication.entity.Client;
import com.algostyle.authentication.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientRestController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private AuthenticationManager authManager;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Client client){
        UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(client.getEmail(),client.getMotDePasse());
        Authentication authenticate=authManager.authenticate(token);
        boolean status=authenticate.isAuthenticated();
        if(status){
            return new ResponseEntity<>("login successfully", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("login failed",HttpStatus.BAD_REQUEST);
        }
    }



    @PostMapping("/register")
    public ResponseEntity<String> registerClient(@RequestBody Client client){
        boolean status=clientService.saveClient(client);
        if(status){
            return new ResponseEntity<>("Register successfully",HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("Registration failed",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
