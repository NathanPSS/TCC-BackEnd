package com.hujb.app.usuarios.estagiarios.controllers;


import com.hujb.app.config.auth.jwt.dto.AuthRequestBody;
import com.hujb.app.config.auth.jwt.dto.Token;
import com.hujb.app.registros.dto.CheckIn;
import com.hujb.app.registros.dto.CheckInClose;
import com.hujb.app.setores.Setor;
import com.hujb.app.usuarios.estagiarios.dto.EstagiarioJSON;
import com.hujb.app.usuarios.estagiarios.dto.RegistryRequestDTO;
import com.hujb.app.usuarios.estagiarios.dto.FindByMatricula;
import com.hujb.app.usuarios.estagiarios.services.EstagiariosService;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/estagiario")
public class EstagiariosController {

    private final EstagiariosService service;
    public EstagiariosController(EstagiariosService service) {
        this.service = service;
    }

    @PostMapping
    @PermitAll
    public ResponseEntity<EstagiarioJSON> getEstagiario (@RequestBody FindByMatricula request) {
        return ResponseEntity.status(200).body(service.findByMatricula(request.matricula()));
    };

    @PostMapping("/create")
    @PermitAll
    public ResponseEntity create(@RequestBody RegistryRequestDTO request){
         service.create(request);
         return ResponseEntity.status(201).build();
    };


    @PostMapping("/auth")
    @PermitAll
    public  ResponseEntity<Token> authenticate(@RequestBody AuthRequestBody dto){
     return ResponseEntity.status(200).body(service.authenticate(dto));
    };

    @GetMapping("/authdecode")
    public void testdecode(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println(username);
    }
    @PostMapping("/check-in")
    public ResponseEntity<CheckIn> openCheckIn(@RequestBody Setor dto){
       return ResponseEntity.status(200).body(new CheckIn(service.openCheckIn(dto)));
    };

    @PostMapping("/check-out")
    public ResponseEntity<Void> closeCheckIn(){
      service.closeCheckIn();
      return ResponseEntity.status(200).build();
    }

}
