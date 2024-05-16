package com.hujb.app.usuarios.estagiarios.repositories;

import com.hujb.app.registros.entities.Registro;
import com.hujb.app.usuarios.entities.Usuario;
import com.hujb.app.usuarios.estagiarios.dto.EstagiarioSummary;
import com.hujb.app.usuarios.estagiarios.dto.RegistryRejected;
import com.hujb.app.usuarios.estagiarios.dto.RegistrySigned;
import com.hujb.app.usuarios.estagiarios.dto.RegistryWithoutSIgn;
import com.hujb.app.usuarios.estagiarios.entities.Estagiario;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EstagiariosRepository extends JpaRepository<Estagiario,String> {

   @Query("SELECT NEW com.hujb.app.usuarios.estagiarios.dto.EstagiarioSummary(e.matricula,u.nome,u.id)\n" +
           "FROM Estagiario e\n" +
           "LEFT JOIN Usuario u\n" +
           "ON e.usuario.id = u.id\n" +
           "WHERE e.matricula = :matricula AND e.deleted = false")
   Optional<EstagiarioSummary> findSummaryByMatricula(String matricula);

   @Modifying
   @Query("UPDATE Estagiario e SET e.deleted = true WHERE e.matricula =:matricula")
   void softDeleteByMatricula(@Param("matricula") String matricula);


   Optional<String> findUsuarioNomeByMatricula(String id);

   @Query(value = "SELECT u.nome as nome,hr_entrada,hr_saida,tempo,descricao,s.nome as setor_nome\n" +
           "FROM registro_em_analise r\n" +
           "LEFT JOIN estagiario e\n" +
           "ON r.estagiario_matricula = e.matricula \n" +
           "LEFT JOIN usuario u\n" +
           "ON e.usuario_id = u.id\n" +
           "LEFT JOIN setor s\n" +
           "ON s.id = r.setor_id\n" +
           "WHERE matricula=?1", nativeQuery = true)
   List<Tuple> getAllRegistriesWithoutSing(String username);

   @Query(value = "SELECT u_estagiario.nome as nome,hr_entrada,hr_saida,tempo,descricao,s.nome as setor_nome,u_preceptor.nome as preceptor_nome\n" +
           "           FROM registro_assinado ra\n" +
           "           LEFT JOIN registro r\n" +
           "           ON ra.registro_id = r.id\n" +
           "           LEFT JOIN estagiario e\n" +
           "           ON r.estagiario_matricula = e.matricula \n" +
           "           LEFT JOIN usuario u_estagiario\n" +
           "           ON e.usuario_id = u_estagiario.id\n" +
           "           LEFT JOIN setor s\n" +
           "           ON s.id = r.setor_id\n" +
           "\t\t   LEFT JOIN preceptor p\n" +
           "\t\t   ON p.matricula = ra.preceptor_matricula\n" +
           "           LEFT JOIN usuario u_preceptor\n" +
           "           ON u_preceptor.id = p.usuario_id\n" +
           "           WHERE e.matricula=?1 AND e.deleted = false",nativeQuery = true)
   List<Tuple> getAllRegistriesSigned(String username);

   @Query(value="SELECT u_estagiario.nome as nome,hr_entrada,hr_saida,tempo,descricao,s.nome as setor_nome,u_preceptor.nome as preceptor_nome,motivo,menssagem\n" +
           "           FROM registro_rejeitado ra\n" +
           "           LEFT JOIN registro r\n" +
           "           ON ra.registro_id = r.id\n" +
           "           LEFT JOIN estagiario e\n" +
           "           ON r.estagiario_matricula = e.matricula\n" +
           "           LEFT JOIN usuario u_estagiario\n" +
           "           ON e.usuario_id = u_estagiario.id\n" +
           "           LEFT JOIN setor s\n" +
           "           ON s.id = r.setor_id\n" +
           "\t\t   LEFT JOIN preceptor p\n" +
           "\t\t   ON p.matricula = ra.preceptor_matricula\n" +
           "           LEFT JOIN usuario u_preceptor\n" +
           "           ON u_preceptor.id = p.usuario_id\n" +
           "           WHERE e.matricula=?1 AND e.deleted = false",nativeQuery = true)
   List<Tuple> getAllRegistriesRejected(String matricula);

   @Query("SELECT e.usuario.nome FROM Estagiario e WHERE e.matricula = :matricula AND e.deleted = false")
   Optional<String> findNomeByMatricula(String matricula);

}
