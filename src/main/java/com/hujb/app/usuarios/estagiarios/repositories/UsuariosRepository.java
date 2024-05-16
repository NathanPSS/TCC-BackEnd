package com.hujb.app.usuarios.estagiarios.repositories;

import com.hujb.app.usuarios.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuario,Long> {

    Optional<String> findNomeById(Long id);
}
