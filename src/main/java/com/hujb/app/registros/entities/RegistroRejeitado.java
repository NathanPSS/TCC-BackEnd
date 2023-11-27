package com.hujb.app.registros.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hujb.app.usuarios.preceptor.entities.Preceptor;
import jakarta.persistence.*;

@Entity
public class RegistroRejeitado {

    @OneToOne(cascade = {CascadeType.REMOVE})
    @PrimaryKeyJoinColumn(name = "registroId",referencedColumnName = "id")
    @MapsId
    private Registro registro;
    @Id
    @JsonIgnore
    private String registroId;

    @OneToOne(cascade = {CascadeType.REMOVE})
    private Preceptor preceptor;

    @Column
    private String menssagem;

    @Column
    private String motivo;

    public RegistroRejeitado(Registro registro, Preceptor preceptor, String menssagem, String motivo) {
        this.registro = registro;
        this.preceptor = preceptor;
        this.menssagem = menssagem;
        this.motivo = motivo;
    }
}
