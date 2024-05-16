package com.hujb.app.setores;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SetorRepository extends JpaRepository<Setor,Long> {
    @Modifying
    @Query("UPDATE Setor s SET s.deleted = true WHERE s.id =:id")
    void softDeleteById(@Param("id") Long id);
    Optional<String> findNomeById(Long id);
}
