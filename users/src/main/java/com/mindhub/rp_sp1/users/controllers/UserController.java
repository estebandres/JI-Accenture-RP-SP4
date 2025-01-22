package com.mindhub.rp_sp1.users.controllers;

import com.mindhub.rp_sp1.users.dtos.PatchSiteUserDTO;
import com.mindhub.rp_sp1.users.dtos.CreateSiteUserDTO;
import com.mindhub.rp_sp1.users.exceptions.UserNotFoundException;
import com.mindhub.rp_sp1.users.models.SiteUser;
import com.mindhub.rp_sp1.users.services.SiteUserService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    SiteUserService userService;

    @GetMapping("/{id}")
    public SiteUser getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping
    public List<SiteUser> getUserByEmail(@RequestParam(required = false) String email) {
        if(email == null || email.isEmpty()) {
            return userService.findAll();
        }
        return List.of(userService.findByEmail(email));
    }

    @PostMapping
    public SiteUser createUser(@Valid @RequestBody CreateSiteUserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PutMapping("/{id}")
    public SiteUser updateUser(@PathVariable Long id, @Valid@RequestBody CreateSiteUserDTO userDTO) throws UserNotFoundException, BadRequestException {
        return userService.updateUser(id, userDTO);
    }

    @PatchMapping("/{id}")
    public SiteUser updateSomeAttributes(@PathVariable Long id, @Valid @RequestBody PatchSiteUserDTO userDTO) throws UserNotFoundException, BadRequestException {
        return userService.updateSomeAttributes(id, userDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserWithId(@PathVariable Long id) throws UserNotFoundException {
        userService.deleteUserWithId(id);
        return ResponseEntity.ok("User with id: " + id + " was deleted");
    }
}
