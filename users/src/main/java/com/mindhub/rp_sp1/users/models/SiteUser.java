package com.mindhub.rp_sp1.users.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SiteUser {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;


    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private List<RoleType> roles = new ArrayList<>();

    public SiteUser(String username, String email, List<RoleType> roles) {
        this.username = username;
        this.email = email;
        this.roles = roles;
    }


    public void addRole(RoleType role) {
        this.roles.add(role);
    }
    public void removeRole(RoleType role) {
        this.roles.remove(role);
    }

}
