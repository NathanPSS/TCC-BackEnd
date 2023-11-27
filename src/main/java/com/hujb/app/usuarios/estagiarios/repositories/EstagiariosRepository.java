package com.hujb.app.usuarios.estagiarios.repositories;

import com.hujb.app.registros.entities.Registro;
import com.hujb.app.usuarios.entities.Usuario;
import com.hujb.app.usuarios.estagiarios.dto.RegistryRejected;
import com.hujb.app.usuarios.estagiarios.dto.RegistrySigned;
import com.hujb.app.usuarios.estagiarios.dto.RegistryWithoutSIgn;
import com.hujb.app.usuarios.estagiarios.entities.Estagiario;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EstagiariosRepository extends JpaRepository<Estagiario,Usuario> {
   Estagiario findByMatricula(String matricula);

   @Query("SELECT e FROM Estagiario e WHERE e.matricula = :username")
   Optional<Estagiario> findByUsername(String username);


   @Query(value = "SELECT u.nome as nome,hr_entrada as hrEntrada,hr_saida as hrSaida,tempo,descricao,s.nome as setorNome\n" +
           "FROM registro_em_analise r\n" +
           "LEFT JOIN estagiarios e\n" +
           "ON r.id_estagiario = e.usuario_id \n" +
           "LEFT JOIN usuarios u\n" +
           "ON e.usuario_id = u.id\n" +
           "LEFT JOIN setor s\n" +
           "ON s.id = r.setor\n" +
           "WHERE matricula=?1", nativeQuery = true)
   List<Tuple> getAllRegistriesWithoutSing(String username);

   @Query(value = "SELECT u_estagiario.nome as nome,hr_entrada as hrEntrada,hr_saida as hrSaida,tempo,descricao,s.nome as setorNome,u_preceptor.nome as preceptorNome\n" +
           "FROM registro_assinado ra\n" +
           "LEFT JOIN registro r\n" +
           "ON ra.registro_id = r.id\n" +
           "LEFT JOIN estagiarios e\n" +
           "ON r.id_estagiario = e.usuario_id \n" +
           "LEFT JOIN usuarios u_estagiario\n" +
           "ON e.usuario_id = u_estagiario.id\n" +
           "LEFT JOIN setor s\n" +
           "ON s.id = r.setor\n" +
           "LEFT JOIN usuarios u_preceptor\n" +
           "ON u_preceptor.id = ra.preceptor_usuario_id\n" +
           "WHERE e.matricula=?1",nativeQuery = true)
   List<Tuple> getAllRegistriesSigned(String username);

   @Query(value = "SELECT u_estagiario.nome as nome,hr_entrada as hrEntrada,hr_saida as hrSaida,tempo,descricao,s.nome as setorNome,u_preceptor.nome as preceptorNome,motivo,menssagem\n" +
           "FROM registro_rejeitado ra\n" +
           "LEFT JOIN registro r\n" +
           "ON ra.registro_id = r.id\n" +
           "LEFT JOIN estagiarios e\n" +
           "ON r.id_estagiario = e.usuario_id \n" +
           "LEFT JOIN usuarios u_estagiario\n" +
           "ON e.usuario_id = u_estagiario.id\n" +
           "LEFT JOIN setor s\n" +
           "ON s.id = r.setor\n" +
           "LEFT JOIN usuarios u_preceptor\n" +
           "ON u_preceptor.id = ra.preceptor_usuario_id\n" +
           "WHERE e.matricula=?1", nativeQuery = true)
   List<Tuple> getAllRegistriesRejected(String username);
}
