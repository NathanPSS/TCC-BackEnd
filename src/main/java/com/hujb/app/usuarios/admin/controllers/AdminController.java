package com.hujb.app.usuarios.admin.controllers;

import com.hujb.app.config.auth.AuthRequestBody;
import com.hujb.app.config.auth.jwt.Token;
import com.hujb.app.usuarios.admin.dto.CreateAdminDTO;
import com.hujb.app.usuarios.admin.services.AdminService;
import com.hujb.app.usuarios.estagiarios.dto.CreateEstagiarioDTO;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    @PostMapping("/auth")
    @PermitAll
    public ResponseEntity<Token> authenticate(@RequestBody AuthRequestBody dto){

        return ResponseEntity.status(200).body(service.authenticate(dto));
    };

    @PostMapping("/create")
    @PermitAll
    public ResponseEntity<Void> create(@RequestBody CreateAdminDTO request){
        service.create(request);
        return ResponseEntity.status(201).build();
    };
}
