package com.hujb.app.registros.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.annotation.Immutable;

@Entity
@Immutable
public class RegistroEmAnalise {
    @OneToOne(cascade = {CascadeType.ALL})
    @PrimaryKeyJoinColumn(name = "registroId",referencedColumnName = "id")
    @MapsId
    private Registro registro;
    @Id
    @JsonIgnore
    private String registroId;
}
