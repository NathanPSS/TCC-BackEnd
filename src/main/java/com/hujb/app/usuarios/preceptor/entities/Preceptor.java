package com.hujb.app.usuarios.preceptor.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hujb.app.setores.Setor;
import com.hujb.app.usuarios.entities.Usuario;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
public class Preceptor implements UserDetails {
    @OneToOne(cascade = {CascadeType.ALL})
    @PrimaryKeyJoinColumn(name = "usuarioId",referencedColumnName = "id")
    @MapsId
    private Usuario usuario;

    @Id
    @JsonIgnore
    private Long usuarioId;

    @Column(name = "matricula",nullable = false,length = 500)
    private String matricula;

    @ManyToOne
    @JoinColumn(name = "setor",referencedColumnName = "id",nullable = false)
    private Setor setor;

    @Column(name = "password")
    private String password;

    public Preceptor(Usuario usuario,String matricula, Setor setor, String password) {
        this.usuario = usuario;
        this.matricula = matricula;
        this.setor = setor;
        this.password = password;
    }

    public Preceptor() {
    }

    public Setor getSetor() {
        return setor;
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
