package com.hujb.app.registros.entities;


import com.hujb.app.usuarios.preceptor.entities.Preceptor;
import jakarta.persistence.*;


@Entity
public class RegistroAssinado {
    @Id
    @OneToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "id_registro",referencedColumnName = "id")
    private Registro registro;
    @OneToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "id",referencedColumnName = "id")
    private Preceptor preceptor;
}
