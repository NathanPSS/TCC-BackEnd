package com.hujb.app.usuarios.admin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hujb.app.usuarios.entities.Usuario;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
public class Administracao implements UserDetails {
    @Column(name = "matricula",nullable = false,length = 500)
    private String matricula;
    @OneToOne(cascade = {CascadeType.REMOVE})
    @PrimaryKeyJoinColumn(name = "usuarioId",referencedColumnName = "id")
    @MapsId
    private Usuario usuario;
    @Id
    @JsonIgnore
    private Long usuarioId;

    @Column(name = "password")
    private String password;

    public Administracao() {

    }

    public Administracao(Usuario usuario, String matricula, String password) {
        this.matricula = matricula;
        this.usuario = usuario;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(usuario.getRole().name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return matricula;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
