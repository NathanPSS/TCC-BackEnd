package com.hujb.app.usuarios.preceptor.dto;


import com.hujb.app.usuarios.estagiarios.dto.EstagiarioSummary;
import jakarta.persistence.Tuple;

public record PreceptorSummary(String matricula, String nome,Long usuarioId,Long setorId,String setorNome) {
    }

