package com.hujb.app.registros.repositories;


import com.hujb.app.registros.entities.Registro;
import com.hujb.app.setores.Setor;
import com.hujb.app.usuarios.estagiarios.entities.Estagiario;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.ResultSet;
import java.util.List;

public interface RegistroRepository extends JpaRepository<Registro,String> {

    List<Registro> findByEstagiario(Long id);

    @Query(value = "SELECT r.id,nome,hr_entrada,hr_saida,tempo,descricao\n" +
            "FROM registro_em_analise r\n" +
            "LEFT JOIN estagiario e\n" +
            "ON r.estagiario_matricula = e.matricula\n" +
            "LEFT JOIN usuario u\n" +
            "ON e.usuario_id = u.id\n" +
            "LEFT JOIN preceptor pr\n" +
            "ON r.setor_id = pr.setor\n" +
            "WHERE pr.matricula = ?1\n"
            ,nativeQuery = true
    )
    List<Tuple> findAllRegistriesForSing(String username);

    List<Registro> findByEstagiario(Estagiario estagiario);

    @Query(value = "SELECT rem.* \n" +
            "FROM registro_em_analise rem,preceptor p\n" +
            "WHERE p.matricula = ?1",nativeQuery = true)
    List<Registro>findAllRegistriesForAnalisys(String username);
}
