package com.hujb.app.usuarios.admin.controllers;

import com.hujb.app.config.auth.AuthRequestBody;
import com.hujb.app.config.auth.jwt.Token;
import com.hujb.app.config.redis.RedisMessageListener;
import com.hujb.app.setores.CreateSetorDTO;
import com.hujb.app.setores.IdSetorDTO;
import com.hujb.app.setores.SetorService;
import com.hujb.app.setores.UpdateSetorDTO;
import com.hujb.app.usuarios.admin.dto.AdminSummary;
import com.hujb.app.usuarios.admin.dto.CreateAdminDTO;
import com.hujb.app.usuarios.admin.dto.FindByMatriculaPreceptor;
import com.hujb.app.usuarios.admin.dto.UpdateAdminDTO;
import com.hujb.app.usuarios.admin.services.AdminService;
import com.hujb.app.usuarios.dto.UpdatePasswordDTO;
import com.hujb.app.usuarios.estagiarios.dto.*;
import com.hujb.app.usuarios.estagiarios.services.EstagiarioMessageService;
import com.hujb.app.usuarios.estagiarios.services.EstagiariosService;
import com.hujb.app.usuarios.preceptor.dto.CreatePreceptorDTO;
import com.hujb.app.usuarios.preceptor.dto.PreceptorSummary;
import com.hujb.app.usuarios.preceptor.dto.UpdatePreceptorDTO;

import com.hujb.app.usuarios.services.PreceptorService;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.List;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    private final AdminService serviceAdmin;

    private final PreceptorService servicePreceptor;

    private final EstagiariosService serviceEstagiario;

    private final SetorService serviceSetor;

    private final RedisMessageListener messageListener;

    private final EstagiarioMessageService estagiarioMessageService;



    public AdminController(
            AdminService serviceAdmin,
            EstagiariosService serviceEstagiario,
            PreceptorService servicePreceptor,
            SetorService serviceSetor,
            RedisMessageListener messageListener,
            EstagiarioMessageService estagiarioMessageService) {
        this.serviceAdmin = serviceAdmin;
        this.serviceSetor = serviceSetor;
        this.serviceEstagiario = serviceEstagiario;
        this.servicePreceptor = servicePreceptor;
        this.messageListener = messageListener;
        this.estagiarioMessageService = estagiarioMessageService;
    }

    @PostMapping("/auth")
    @PermitAll
    public ResponseEntity<Token> authenticate(@RequestBody AuthRequestBody dto){
        return ResponseEntity.status(200).body(serviceAdmin.authenticate(dto));
    };

    @PostMapping("/create")
    public ResponseEntity<Void> createAdmin(@RequestBody CreateAdminDTO dto){
        serviceAdmin.create(dto);
        return ResponseEntity.status(201).build();
    };
    @PostMapping
    public ResponseEntity<AdminSummary> findAdmin(@RequestBody CreateAdminDTO dto){
        return ResponseEntity.status(200).body(serviceAdmin.findAdmin(dto.matricula()));
    };

    @PutMapping
    public ResponseEntity<Void> editAdmin(@RequestBody UpdateAdminDTO dto) {
        serviceAdmin.update(dto.matricula(),dto.nome(),dto.usuarioId());
        return ResponseEntity.status(200).build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> editAdminPassword(@RequestBody UpdatePasswordDTO dto){
        serviceAdmin.updatePassword(dto.matricula(),dto.password());
        return ResponseEntity.status(201).build();
    };
    @DeleteMapping
    public ResponseEntity<Void> deleteAdmin(@RequestBody FindByMatricula dto){
        serviceAdmin.remove(dto.matricula());
        return ResponseEntity.status(200).build();
    };

    // CRUD Estagiario
    @PostMapping("/estagiario/create")
    public ResponseEntity<Void> createEstagiario(@RequestBody CreateEstagiarioDTO dto){
        serviceEstagiario.create(dto.nome(),dto.matricula(),dto.password());
        return ResponseEntity.status(201).build();
    };

    @PostMapping("/estagiario")
    public ResponseEntity<EstagiarioSummary> findEstagiario(@RequestBody FindByMatricula request){
        var estagiarioSummary = serviceEstagiario.findSummaryByMatricula(request.matricula());
        return ResponseEntity.status(200).body(estagiarioSummary);
    };

    @PostMapping("/estagiario/signedRegistries")
    public ResponseEntity<List<RegistrySigned>> findAllSignedRegistries(@RequestBody FindByMatricula request){
        return ResponseEntity.status(200).body(serviceEstagiario.getAllSignedRegistries(request.matricula()));
    }
    @PostMapping("/estagiario/rejectedRegistries")
    public ResponseEntity<List<RegistryRejected>> findAllRejectedRegistries(@RequestBody FindByMatricula request){

        return ResponseEntity.status(200).body(serviceEstagiario.getAllRejectedRegistries(request.matricula()));
    }
    @PostMapping("/estagiario/withoutSignRegistries")
    public ResponseEntity<List<RegistryWithoutSIgn>> findAllWithoutSignRegistries(@RequestBody FindByMatricula request){
        return ResponseEntity.status(200).body(serviceEstagiario.getAllWithoutSingRegistries(request.matricula()));
    }

    @PutMapping("/estagiario")
    public ResponseEntity<Void> editEstagiario(@RequestBody UpdateEstagiarioDTO dto){
        serviceEstagiario.update(dto.matricula(),dto.nome(),dto.usuarioId());
        return ResponseEntity.status(201).build();
    };

    @PutMapping("/estagiario/password")
    public ResponseEntity<Void> editEstagiarioPassword(@RequestBody UpdatePasswordDTO dto){
        serviceEstagiario.updatePassword(dto.matricula(),dto.password());
        return ResponseEntity.status(201).build();
    };

    @DeleteMapping("/estagiario")
    public ResponseEntity<Void> removeEstagiario(@RequestBody FindByMatricula matricula){
        serviceEstagiario.remove(matricula.matricula());
        return ResponseEntity.status(200).build();
    };

    @GetMapping(path = "/web-flux",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamFlux(){
        return messageListener.emitter();
    }

    @GetMapping("estagiario-context")
    public List<EstagiarioEvent> getEstagiarioContext(){
        return estagiarioMessageService.getEstagiarioEvent();
    }

    // END

    @PostMapping("/preceptor/create")
    public ResponseEntity<Void> createPreceptor(@RequestBody CreatePreceptorDTO dto){
        servicePreceptor.create(dto);
        return ResponseEntity.status(201).build();
    };

    @PostMapping("/preceptor")
    public ResponseEntity<PreceptorSummary> findPreceptor(@RequestBody FindByMatriculaPreceptor matricula){

        var preceptorSummary = servicePreceptor.findSummaryByMatricula(matricula.matricula());
        return ResponseEntity.status(200).body(preceptorSummary);
    };
    @PutMapping("/preceptor")
    public ResponseEntity<Void> editPreceptor(@RequestBody UpdatePreceptorDTO dto){
        servicePreceptor.update(dto.matricula(),dto.nome(),dto.idSetor(),dto.usuarioId());
        return ResponseEntity.status(201).build();
    };

    @PutMapping("/preceptor/password")
    public ResponseEntity<Void> editPreceptorPassword(@RequestBody UpdatePasswordDTO dto){
        servicePreceptor.updatePassword(dto.matricula(),dto.password());
        return ResponseEntity.status(201).build();
    };
    @DeleteMapping("/preceptor")
    public ResponseEntity<Void> removePreceptor(@RequestBody FindByMatricula matricula){
        servicePreceptor.remove(matricula.matricula());
        return ResponseEntity.status(200).build();
    };

    @PostMapping("/setor")
    public ResponseEntity<Void> createSetor(@RequestBody CreateSetorDTO dto) {
        serviceSetor.create(dto);
        return ResponseEntity.status(200).build();
    }

    @PutMapping("/setor")
    public ResponseEntity<Void> editSetor(@RequestBody UpdateSetorDTO dto){
       serviceSetor.update(dto.nome(),dto.setorId());
       return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/setor")
    public ResponseEntity<Void> deleteSetor(@RequestBody IdSetorDTO dto){
        serviceSetor.remove(dto.idSetor());
        return ResponseEntity.status(200).build();
    }
}
