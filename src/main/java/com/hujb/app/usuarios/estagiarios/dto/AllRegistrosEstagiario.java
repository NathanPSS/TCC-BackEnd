package com.hujb.app.usuarios.estagiarios.dto;

import com.hujb.app.registros.entities.Registro;
import com.hujb.app.registros.entities.RegistroAssinado;
import com.hujb.app.registros.entities.RegistroRejeitado;

import java.util.List;

public record AllRegistrosEstagiario(List<RegistryWithoutSIgn> registrosSemAssinaturas, List<RegistrySigned> registroAssinados, List<RegistryRejected> registroRejeitados) {
}
