package com.hujb.app.registros.dto;

import java.io.Serializable;

public record RegistryUI(String id, String nome, String date, String hr_entrada, String hr_saida, String tempo, String descricao) implements Serializable {
}
