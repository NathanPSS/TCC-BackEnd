package com.hujb.app.usuarios.preceptor.dto;

import com.hujb.app.setores.Setor;
import com.hujb.app.usuarios.entities.Usuario;

public record CreatePreceptorDTO(String nome,String matricula,String password,Long idSetor) { }
