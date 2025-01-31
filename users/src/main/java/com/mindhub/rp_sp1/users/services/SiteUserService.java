package com.mindhub.rp_sp1.users.services;

import com.mindhub.rp_sp1.users.dtos.*;
import com.mindhub.rp_sp1.users.exceptions.UserAlreadyExistsException;
import com.mindhub.rp_sp1.users.exceptions.UserNotFoundByEmailException;
import com.mindhub.rp_sp1.users.exceptions.UserNotFoundException;
import com.mindhub.rp_sp1.users.models.SiteUser;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface SiteUserService {
    List<GetUserDTO> findAll();
    GetUserDTO findById(Long id);
    GetUserDTO findByEmail(String email) throws UserNotFoundByEmailException;
    GetUserDTO createUser(CreateSiteUserDTO user);
    GetUserDTO updateUser(Long id, CreateSiteUserDTO user) throws UserNotFoundException, BadRequestException;
    GetUserDTO updateSomeAttributes(Long id, PatchSiteUserDTO user) throws UserNotFoundException, BadRequestException;
    void deleteUserWithId(Long id) throws UserNotFoundException;

    GetUserDTO registerNewUser(RegisterUserDTO registerUserDTO) throws UserAlreadyExistsException;
}
