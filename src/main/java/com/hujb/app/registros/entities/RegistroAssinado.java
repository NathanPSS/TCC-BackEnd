package com.hujb.app.registros.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hujb.app.usuarios.preceptor.entities.Preceptor;
import jakarta.persistence.*;

@Entity
public class RegistroAssinado {

    @OneToOne(cascade = {CascadeType.ALL})
    @PrimaryKeyJoinColumn(name = "registroId",referencedColumnName = "id")
    @MapsId
    private Registro registro;
    @Id
    @JsonIgnore
    private String registroId;

    @OneToOne(cascade = {CascadeType.REMOVE})
    private Preceptor preceptor;

    public RegistroAssinado(Registro registro, Preceptor preceptor) {
        this.registro = registro;
        this.preceptor = preceptor;
    }

    public RegistroAssinado(String registroId,Long preceptorId){

    }
}
