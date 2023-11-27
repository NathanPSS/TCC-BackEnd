package com.hujb.app.registros.dto;

import com.hujb.app.registros.entities.Registro;

public record RegistryRejected(String idRegistry, String menssage, String reason) {
}
