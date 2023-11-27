package com.hujb.app.usuarios.admin.repositories;

import com.hujb.app.usuarios.admin.entities.Administracao;
import com.hujb.app.usuarios.entities.Usuario;
import com.hujb.app.usuarios.preceptor.entities.Preceptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Administracao, Usuario> {
    @Query("SELECT a FROM Administracao a WHERE a.matricula = :username")
    Optional<Administracao> findByUsername(String username);
}
