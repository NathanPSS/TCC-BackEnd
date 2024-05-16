package com.hujb.app.usuarios.admin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hujb.app.usuarios.entities.Usuario;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Where(clause = "deleted=false")
public class Administracao implements UserDetails {
    @Id
    @Column(name = "matricula",length = 500)
    private String matricula;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "usuarioId",referencedColumnName = "id")
    private Usuario usuario;

    private String password;

    private Boolean deleted = Boolean.FALSE;

    public Administracao() {

    }

    public Administracao(Usuario usuario, String matricula, String password) {
        this.matricula = matricula;
        this.usuario = usuario;
        this.password = password;
    }
    public Administracao(Usuario usuario, String matricula) {
        this.matricula = matricula;
        this.usuario = usuario;
    }

    public Administracao(String matricula,String password){
        this.matricula = matricula;
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
