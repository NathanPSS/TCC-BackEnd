package com.hujb.app.usuarios.estagiarios.repositories;

import com.hujb.app.usuarios.entities.Usuario;
import com.hujb.app.usuarios.estagiarios.entities.Estagiario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EstagiariosRepository extends JpaRepository<Estagiario,Usuario> {
   Estagiario findByMatricula(String matricula);

   @Query("SELECT e FROM Estagiario e WHERE e.matricula = :username")
   Optional<Estagiario> findByUsername(String username);

}
