package com.hujb.app.usuarios.estagiarios.controllers;


import com.hujb.app.config.auth.AuthRequestBody;
import com.hujb.app.config.auth.jwt.Token;

import com.hujb.app.registros.dto.InfoCheckIn;
import com.hujb.app.setores.Setor;
import com.hujb.app.usuarios.estagiarios.dto.*;
import com.hujb.app.usuarios.estagiarios.services.EstagiariosService;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/estagiario")
public class EstagiariosController {

    private final EstagiariosService service;
    public EstagiariosController(EstagiariosService service) {
        this.service = service;
    }

    @PostMapping
    @PermitAll
    public ResponseEntity<EstagiarioSummary> getEstagiario (@RequestBody FindByMatricula request) {
        return ResponseEntity.status(200).body(service.findSummaryByMatricula(request.matricula()));
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
    public ResponseEntity<Void> openCheckIn(@RequestBody Setor dto){
        service.openCheckIn(dto);
       return ResponseEntity.status(200).build();
    };

    @PostMapping("/check-out")
    public ResponseEntity<InfoCheckIn> closeCheckIn(){
      return ResponseEntity.status(200).body(service.closeCheckIn());
    }

    @PostMapping("/check-in-time")
    public ResponseEntity<String> getCurrentTime(){
        return ResponseEntity.status(200).body(service.getCurrentTimeCheckIn());
    }

    @PostMapping("/registry")
    public ResponseEntity<Void> addRegistry(@RequestBody Map<String,String> dto){
        service.createRegistry(dto.get("description"));
      return ResponseEntity.status(200).build();
    };

    @PostMapping("/is-checkIn-open")
    public ResponseEntity<Void> isCheckInOpen(){
        if(service.isCheckInOpen()){
            return ResponseEntity.status(200).build();
        }
       return ResponseEntity.status(404).build();
    }

    @GetMapping("/allRegistries")
    public ResponseEntity<AllRegistrosEstagiario> getAllRegistries(){
        return ResponseEntity.status(200).body(service.getAllRegistries());
    }

}
