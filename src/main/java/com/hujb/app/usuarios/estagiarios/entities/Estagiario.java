package com.hujb.app.usuarios.estagiarios.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hujb.app.usuarios.entities.Usuario;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Where(clause = "deleted=false")
public class Estagiario implements UserDetails {

    // Data Fields
    @Id
    @Column(name = "matricula",length = 500)
    private String matricula;
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "usuarioId",referencedColumnName = "id")
    private Usuario usuario;

    private String password;

    private Boolean deleted = Boolean.FALSE;
    // Constructors
    public Estagiario(){}

    public Estagiario(Usuario usuario,String matricula,String password) {
        this.matricula = matricula;
        this.usuario = usuario;
        this.password = password;
    }
    public Estagiario(Usuario usuario,String matricula) {
        this.matricula = matricula;
        this.usuario = usuario;
    }

    public Estagiario(String matricula,String password){
        this.matricula = matricula;
        this.password = password;
    }

    public Estagiario(Usuario usuario,String matricula,String password,Boolean deleted) {
        this.matricula = matricula;
        this.usuario = usuario;
        this.password = password;
        this.deleted = deleted;
    }
    public Estagiario(String matricula) {
        this.matricula = matricula;
    }

    // Setters
    private void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private void setPassword(String password) {this.password = password;}

    private void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    // Getters
    public String getMatricula() { return matricula; }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getPassword() { return password; }

    // UserDetails IMPL
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(usuario.getRole().name()));
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

    // toString and hashCodeEquals
    @Override
    public String toString() {
        return "Estagiario{" +
                ", usuario=" + usuario +
                ", matricula='" + matricula + '\'' +
                '}';
    }
}
