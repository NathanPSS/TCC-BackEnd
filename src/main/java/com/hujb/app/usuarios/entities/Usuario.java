package com.hujb.app.usuarios.entities;


import com.hujb.app.usuarios.enums.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
public class Usuario  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private  String nome;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    public Usuario(Long id) {
        this.id = id;
    }
    public Usuario(String nome) {
        this.nome = nome;
    }
    public Usuario(Long id,String nome) {
        this.id = id;
        this.nome = nome;
    }
    public Usuario(String nome,Role role) {
        this.nome = nome;
        this.role = role;
    }
    public Usuario(){}
    private void setId(Long id) {
        this.id = id;
    }

    private void setNome(String nome) {
        this.nome = nome;
    }

    private void setRole(Role role) { this.role = role; }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Role getRole() { return role; }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }

}
