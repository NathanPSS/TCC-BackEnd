package com.hujb.app.setores;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/setor")
public class SetorController {

    private final SetorService service;

    public SetorController(SetorService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Setor>> getAll(){
        return ResponseEntity.status(200).body(service.getAll());
    }

}
