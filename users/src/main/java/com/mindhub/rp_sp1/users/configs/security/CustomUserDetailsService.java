package com.mindhub.rp_sp1.users.configs.security;

import com.mindhub.rp_sp1.users.models.SiteUser;
import com.mindhub.rp_sp1.users.repositories.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SiteUserRepository siteUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("STEVE: loadUserByUsername username: " + username);
        SiteUser userEntity = siteUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        String[] roles = userEntity.getRoles().stream().map(Enum::name).toArray(String[]::new);
        return new User(userEntity.getEmail(), userEntity.getPassword(), AuthorityUtils.createAuthorityList(roles));
    }
}
