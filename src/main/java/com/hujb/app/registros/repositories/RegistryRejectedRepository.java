package com.hujb.app.registros.repositories;

import com.hujb.app.registros.entities.Registro;
import com.hujb.app.registros.entities.RegistroRejeitado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistryRejectedRepository extends JpaRepository<RegistroRejeitado,Registro> {
}
