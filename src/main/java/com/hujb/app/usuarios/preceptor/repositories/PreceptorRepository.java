package com.hujb.app.usuarios.preceptor.repositories;

import com.hujb.app.usuarios.entities.Usuario;
import com.hujb.app.usuarios.estagiarios.entities.Estagiario;
import com.hujb.app.usuarios.preceptor.entities.Preceptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface PreceptorRepository extends JpaRepository<Preceptor, Usuario> {

    @Query("SELECT p FROM Preceptor p WHERE p.matricula = :username")
    Optional<Preceptor> findByUsername(String username);

}
