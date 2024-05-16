package com.hujb.app.usuarios.preceptor.controllers;

import com.hujb.app.config.auth.AuthRequestBody;
import com.hujb.app.config.auth.jwt.Token;

import com.hujb.app.registros.dto.RegistryUI;
import com.hujb.app.registros.dto.RegistryRejected;
import com.hujb.app.usuarios.estagiarios.dto.AllRegistrosEstagiario;
import com.hujb.app.usuarios.preceptor.dto.CreatePreceptorDTO;
import com.hujb.app.usuarios.services.PreceptorService;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/preceptor")
public class PreceptorController {

    private final PreceptorService preceptorService;

    public PreceptorController(
            PreceptorService preceptorService
    ) {
        this.preceptorService = preceptorService;
    }

    @PostMapping("/auth")
    @PermitAll
    public ResponseEntity<Token> authenticate(@RequestBody AuthRequestBody dto) {
        return ResponseEntity.status(200).body(preceptorService.authenticate(dto));
    };

    @PostMapping("/sign")
    public ResponseEntity<Void> sign(){
        preceptorService.saveSignedRegistries();
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/registries")
    public ResponseEntity<List<RegistryUI>> registries() throws SQLException {
        return ResponseEntity.status(200).body(preceptorService.findAllRegistries());
    }

    @PostMapping("/rejectRegistry")
    public ResponseEntity<Void> rejectRegistry(@RequestBody RegistryRejected dto){
        preceptorService.saveRejectedRegistry(dto);
        return ResponseEntity.status(200).build();
    }

}
