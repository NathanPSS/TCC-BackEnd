package com.hujb.app.usuarios.estagiarios.repositories;

import com.hujb.app.usuarios.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariosRepository extends JpaRepository<Usuario,Long> {
}
