package com.hujb.app.usuarios.admin.repositories;

import com.hujb.app.usuarios.admin.dto.AdminSummary;
import com.hujb.app.usuarios.admin.entities.Administracao;
import com.hujb.app.usuarios.entities.Usuario;
import com.hujb.app.usuarios.estagiarios.dto.EstagiarioSummary;
import com.hujb.app.usuarios.preceptor.entities.Preceptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Administracao, String> {
    @Query("SELECT a FROM Administracao a WHERE a.matricula = :username AND a.deleted = false")
    Optional<Administracao> findByUsername(String username);


    @Query("SELECT NEW com.hujb.app.usuarios.admin.dto.AdminSummary(a.matricula,u.nome,u.id)\n" +
            "FROM Administracao a\n" +
            "LEFT JOIN Usuario u\n" +
            "ON a.usuario.id = u.id\n" +
            "WHERE a.matricula = :matricula AND a.deleted = false")
    Optional<AdminSummary> findSummaryByMatricula(String matricula);
    @Modifying
    @Query("UPDATE Administracao a SET a.deleted = true WHERE a.matricula =:matricula")
    void softDeleteByMatricula(@Param("matricula") String matricula);
}
