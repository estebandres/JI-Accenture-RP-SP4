package com.mindhub.rp_sp1.users.services;

import com.mindhub.rp_sp1.users.dtos.PatchSiteUserDTO;
import com.mindhub.rp_sp1.users.dtos.RegisterUserDTO;
import com.mindhub.rp_sp1.users.dtos.SiteUserDto;
import com.mindhub.rp_sp1.users.exceptions.UserAlreadyExistsException;
import com.mindhub.rp_sp1.users.exceptions.UserNotFoundByEmailException;
import com.mindhub.rp_sp1.users.exceptions.UserNotFoundException;
import com.mindhub.rp_sp1.users.models.SiteUser;
import com.mindhub.rp_sp1.users.dtos.CreateSiteUserDTO;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface SiteUserService {
    List<SiteUser> findAll();
    SiteUser findById(Long id);
    SiteUser findByEmail(String email) throws UserNotFoundByEmailException;
    SiteUser createUser(CreateSiteUserDTO user);
    SiteUser updateUser(Long id, CreateSiteUserDTO user) throws UserNotFoundException, BadRequestException;
    SiteUser updateSomeAttributes(Long id, PatchSiteUserDTO user) throws UserNotFoundException, BadRequestException;
    void deleteUserWithId(Long id) throws UserNotFoundException;

    SiteUserDto registerNewUser(RegisterUserDTO registerUserDTO) throws UserAlreadyExistsException;
}
