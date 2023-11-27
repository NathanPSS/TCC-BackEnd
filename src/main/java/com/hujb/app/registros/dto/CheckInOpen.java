package com.hujb.app.registros.dto;

import com.hujb.app.setores.Setor;

import java.io.Serializable;

public record CheckInOpen(String username, String createdAT, String id, Setor setor) implements Serializable {

}
