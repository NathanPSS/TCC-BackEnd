package com.hujb.app.registros.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class RegistroDTO {

    @NotNull
    private Long idEstagiario;

    @NotNull
    private LocalDateTime hrEntrada;

    @NotNull
    private LocalDateTime hrSaida;

}
