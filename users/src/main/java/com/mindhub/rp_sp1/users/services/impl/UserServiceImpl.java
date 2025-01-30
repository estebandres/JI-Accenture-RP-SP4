package com.mindhub.rp_sp1.users.services.impl;

import com.mindhub.rp_sp1.users.dtos.PatchSiteUserDTO;
import com.mindhub.rp_sp1.users.dtos.CreateSiteUserDTO;
import com.mindhub.rp_sp1.users.dtos.RegisterUserDTO;
import com.mindhub.rp_sp1.users.dtos.SiteUserDto;
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
    public List<SiteUser> findAll() {
        return siteUserRepository.findAll();
    }

    @Override
    public SiteUser findById(Long id) {
        return siteUserRepository.findById(id).orElse(null);
    }

    @Override
    public SiteUser findByEmail(String email) throws UserNotFoundByEmailException {
        return siteUserRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundByEmailException(email));
    }

    @Override
    public SiteUser createUser(CreateSiteUserDTO user) {
        return siteUserRepository.save(new SiteUser(user.username(), user.email(), new ArrayList<>(user.roles().stream().map(RoleType::valueOf).toList())));
    }


    @Override
    public SiteUser updateSomeAttributes(Long id, PatchSiteUserDTO user) throws UserNotFoundException, BadRequestException {
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
            return siteUserRepository.save(siteUser);
        }
        return null;
    }

    @Override
    public SiteUser updateUser(Long id, CreateSiteUserDTO user) throws UserNotFoundException, BadRequestException {
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
            return siteUserRepository.save(siteUser);
        }
        return null;
    }

    @Override
    public void deleteUserWithId(Long id) throws UserNotFoundException {
        SiteUser siteUser = siteUserRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        siteUserRepository.deleteById(id);
    }

    @Override
    public SiteUserDto registerNewUser(RegisterUserDTO registerUserDTO) throws UserAlreadyExistsException {
        if(siteUserRepository.existsAppUserByEmail(registerUserDTO.email())){
            throw new UserAlreadyExistsException(registerUserDTO.email());
        }
        SiteUser user = new SiteUser();
        user.setEmail(registerUserDTO.email());
        user.setUsername(registerUserDTO.username());
        user.setPassword(passwordEncoder.encode(registerUserDTO.password()));
        SiteUser savedUser = siteUserRepository.save(user);

        return new SiteUserDto(savedUser);
    }
}
