package com.hujb.app.registros.repositories;


import com.hujb.app.registros.entities.Registro;
import com.hujb.app.setores.Setor;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.ResultSet;
import java.util.List;

public interface RegistroRepository extends JpaRepository<Registro,String> {

    List<Registro> findByEstagiario(Long id);

    @Query(value = "SELECT r.id,nome,hr_entrada,hr_saida,tempo,descricao\n" +
            "FROM registro r\n" +
            "LEFT JOIN estagiarios e\n" +
            "ON r.id_estagiario = e.usuario_id\n" +
            "LEFT JOIN usuarios u\n" +
            "ON e.usuario_id = u.id\n" +
            "LEFT JOIN preceptor pr\n" +
            "ON r.setor = pr.setor\n" +
            "WHERE pr.matricula = ?1\n" +
            "AND NOT EXISTS (\n" +
            "SELECT registro_id\n" +
            "FROM registro_rejeitado rr\n" +
            "WHERE r.id = registro_id\n" +
            ")\n" +
            "AND NOT EXISTS (\n" +
            "SELECT registro_id\n" +
            "FROM registro_assinado ra\n" +
            "WHERE r.id = registro_id\n" +
            ")",
            nativeQuery = true
    )
    List<Tuple> findAllRegistriesForSing(String username);


    @Query(value = "SELECT rem.* \n" +
            "FROM registro_em_analise rem,preceptor p\n" +
            "WHERE p.matricula = ?1",nativeQuery = true)
    List<Registro>findAllRegistriesForAnalisys(String username);
}
