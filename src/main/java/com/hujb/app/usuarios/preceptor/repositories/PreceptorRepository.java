package com.hujb.app.usuarios.preceptor.repositories;

import com.hujb.app.usuarios.entities.Usuario;
import com.hujb.app.usuarios.estagiarios.entities.Estagiario;
import com.hujb.app.usuarios.preceptor.dto.PreceptorSummary;
import com.hujb.app.usuarios.preceptor.entities.Preceptor;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface PreceptorRepository extends JpaRepository<Preceptor, String> {

    @Query("SELECT p FROM Preceptor p WHERE p.matricula = :username AND p.deleted = false")
    Optional<Preceptor> findByUsername(String username);

    @Modifying
    @Query("UPDATE Preceptor p SET p.deleted = true WHERE p.matricula =:matricula")
    void softDeleteByMatricula(@Param("matricula") String matricula);

    Optional<String> findUsuarioNomeByMatricula(String id);
    @Query("SELECT NEW com.hujb.app.usuarios.preceptor.dto.PreceptorSummary(p.matricula,u.nome,u.id,s.id,s.nome)\n" +
            "FROM Preceptor p\n" +
            "LEFT JOIN Usuario u\n" +
            "ON p.usuario.id = u.id\n" +
            "LEFT JOIN Setor s\n " +
            "ON p.setor.id = s.id \n" +
            "WHERE p.matricula=:matricula AND p.deleted = false")
    Optional<PreceptorSummary> findSummaryByMatricula(String matricula);


    @Query(value = "UPDATE preceptor SET deleted = true WHERE matricula=?1",nativeQuery = true)
    @Transactional
    void deleteByMatricula(String matricula);

    Optional<Preceptor> findByMatricula(String matricula);

}
