package com.mindhub.rp_sp1.users.services.impl;

import com.mindhub.rp_sp1.users.dtos.*;
import com.mindhub.rp_sp1.users.exceptions.UserAlreadyExistsException;
import com.mindhub.rp_sp1.users.exceptions.UserNotFoundByEmailException;
import com.mindhub.rp_sp1.users.exceptions.UserNotFoundException;
import com.mindhub.rp_sp1.users.models.RoleType;
import com.mindhub.rp_sp1.users.models.SiteUser;
import com.mindhub.rp_sp1.users.repositories.SiteUserRepository;
import com.mindhub.rp_sp1.users.services.SiteUserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements SiteUserService {
    @Autowired
    private SiteUserRepository siteUserRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<GetUserDTO> findAll() {
        return siteUserRepository.findAll().stream().map(GetUserDTO::new).toList();
    }

    @Override
    public GetUserDTO findById(Long id) {
        return siteUserRepository.findById(id).map(GetUserDTO::new).orElse(null);
    }

    @Override
    public GetUserDTO findByEmail(String email) throws UserNotFoundByEmailException {
        return siteUserRepository.findByEmail(email).map(GetUserDTO::new).orElseThrow(() -> new UserNotFoundByEmailException(email));
    }

    @Override
    public GetUserDTO createUser(CreateSiteUserDTO user) {
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername(user.username());
        siteUser.setEmail(user.email());
        if (user.roles() != null && !user.roles().isEmpty()) {
            user.roles().forEach( s -> siteUser.addRole(RoleType.valueOf(s)));
        }
        SiteUser savedUser = siteUserRepository.save(siteUser);
        return new GetUserDTO(savedUser);
    }


    @Override
    public GetUserDTO updateSomeAttributes(Long id, PatchSiteUserDTO user) throws UserNotFoundException, BadRequestException {
        SiteUser siteUser = null;
        if (id != null) {
            if (user.id() != null) {
                if (!id.equals(user.id())) {
                    throw new BadRequestException("Id does not match");
                }
            }
            siteUser = siteUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        }
        if (siteUser != null) {
            if (user.username() != null) {
                siteUser.setUsername(user.username());
            }
            if (user.email() != null) {
                siteUser.setEmail(user.email());
            }
            if (user.roles() != null) {
                siteUser.setRoles(new ArrayList<>(user.roles().stream().map(RoleType::valueOf).toList()));
            }
            return new GetUserDTO(siteUserRepository.save(siteUser));
        }
        return null;
    }

    @Override
    public GetUserDTO updateUser(Long id, CreateSiteUserDTO user) throws UserNotFoundException, BadRequestException {
        SiteUser siteUser = null;
        if (id != null) {
            if (user.id() != null) {
                if (!id.equals(user.id())) {
                    throw new BadRequestException("Id does not match");
                }
            }
            siteUser = siteUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        }
        if (siteUser != null) {
            siteUser.setUsername(user.username());
            siteUser.setEmail(user.email());
            siteUser.setRoles(new ArrayList<>(user.roles().stream().map(RoleType::valueOf).toList()));
            return new GetUserDTO(siteUserRepository.save(siteUser));
        }
        return null;
    }

    @Override
    public void deleteUserWithId(Long id) throws UserNotFoundException {
        SiteUser siteUser = siteUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        siteUserRepository.deleteById(id);
    }

    @Override
    public GetUserDTO registerNewUser(RegisterUserDTO registerUserDTO) throws UserAlreadyExistsException {
        if(siteUserRepository.existsAppUserByEmail(registerUserDTO.email())){
            throw new UserAlreadyExistsException(registerUserDTO.email());
        }
        SiteUser user = new SiteUser();
        user.setEmail(registerUserDTO.email());
        user.setUsername(registerUserDTO.username());
        user.setPassword(passwordEncoder.encode(registerUserDTO.password()));
        if (registerUserDTO.roles() != null && !registerUserDTO.roles().isEmpty()) {
            registerUserDTO.roles().forEach(user::addRole);
        } else {
            user.addRole(RoleType.USER);
        }

        SiteUser savedUser = siteUserRepository.save(user);

        return new GetUserDTO(savedUser);
    }
}
