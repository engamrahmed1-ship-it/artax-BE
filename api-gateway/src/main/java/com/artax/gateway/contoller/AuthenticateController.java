package com.artax.gateway.contoller;

import com.artax.gateway.service.AuthenticateService;
import com.artax.gateway.dto.TokenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authenticate")
@CrossOrigin
public class AuthenticateController {

    @Autowired
    AuthenticateService authenticateService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
//	@PreAuthorize("hasAnyAuthority('INV_LIST')")
    public String getToken(@RequestBody TokenRequest tokenRequest) {
        try {
        System.out.println("username:" +tokenRequest.username());
        System.out.println("password:" +tokenRequest.password());

        String token = authenticateService.getToken(tokenRels quest);
            return token ;
        } catch (Exception e) {
            System.out.println("ERROR calling Keycloak: " + e.getMessage());
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }

    }

}
