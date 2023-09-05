package com.hujb.app.usuarios.preceptor.entities;

import com.hujb.app.usuarios.entities.Usuario;
import jakarta.persistence.*;

@Entity
public class Preceptor {
    @Id
    @OneToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "id",referencedColumnName = "id")
    private Usuario usuario;

    @Column(name = "matricula",nullable = false,length = 500)
    private String matricula;
}
