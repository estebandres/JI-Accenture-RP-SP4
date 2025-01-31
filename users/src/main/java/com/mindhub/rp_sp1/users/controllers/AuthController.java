package com.mindhub.rp_sp1.users.controllers;

import com.mindhub.rp_sp1.users.configs.security.JwtUtils;
import com.mindhub.rp_sp1.users.dtos.GetUserDTO;
import com.mindhub.rp_sp1.users.dtos.RegisterUserDTO;
import com.mindhub.rp_sp1.users.dtos.SiteUserDto;
import com.mindhub.rp_sp1.users.dtos.UserLoginDTO;
import com.mindhub.rp_sp1.users.exceptions.UserAlreadyExistsException;
import com.mindhub.rp_sp1.users.services.SiteUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SiteUserService siteUserService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody @Valid UserLoginDTO loginRequest) {
        System.out.println("STEVE REPORT: authenticateUser loginRequest: " + loginRequest);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    public GetUserDTO createUser(@RequestBody @Valid RegisterUserDTO registerUserDTO) throws UserAlreadyExistsException {
        return siteUserService.registerNewUser(registerUserDTO);
    }
}
