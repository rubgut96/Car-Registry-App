package com.rgutierrez.carregistry.repository.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    @Column(name="name")
    private String name;
    @Column(name="mail", unique = true)
    private String mail;
    @Column(name="password")
    private String password;
    @Column(name="role")
    private String role;
    @Column(name="image")
    private String image;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }
    @Override
    public String getUsername() {
        return mail;
    }
    @Override
    public boolean isAccountNonExpired() {
        return Boolean.TRUE;
    }
    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return Boolean.TRUE;
    }
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE;
    }
}
