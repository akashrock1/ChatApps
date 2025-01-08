package com.example.ChatApp.entity;


import com.example.ChatApp.enums.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.example.ChatApp.mapping.RolePermissionMapping.getRolesMapping;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

@Table(uniqueConstraints={@UniqueConstraint(columnNames={"email"})})
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    @OneToMany(mappedBy = "author",fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<PostEntity> post;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<SessionEntity> session;

//    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Roles> role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
System.out.println(getRolesMapping(role).toString());
        return getRolesMapping(role);

    }
}
