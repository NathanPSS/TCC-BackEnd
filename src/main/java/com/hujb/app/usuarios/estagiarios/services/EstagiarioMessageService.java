package com.hujb.app.usuarios.estagiarios.services;

import com.hujb.app.registros.dto.CheckInClosed;
import com.hujb.app.registros.dto.CheckInOpen;
import com.hujb.app.usuarios.estagiarios.dto.EstagiarioEvent;
import com.hujb.app.usuarios.estagiarios.repositories.EstagiariosRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class EstagiarioMessageService {

    private final EstagiariosRepository repositoryEstagiarios;

    private final RedisTemplate<String,Object> redis;

    public EstagiarioMessageService(EstagiariosRepository repositoryEstagiarios, RedisTemplate<String, Object> redis) {
        this.repositoryEstagiarios = repositoryEstagiarios;
        this.redis = redis;
    }

    public void parseAndSendEvent(Message message, SseEmitter emitter) throws IOException {
        var key = StringUtils.substringAfter(new String(message.getChannel()),":");
        var estagiarioNome = repositoryEstagiarios.findNomeByMatricula(key);
        System.out.println(key);
   //     System.out.println(estagiarioNome.isPresent());
        var value = redis.opsForHash().get(key,key);
        assert value != null;
        System.out.println(value.getClass().getName());
        if(value instanceof CheckInOpen checkInOpen){
             emitter.send(new EstagiarioEvent("open",checkInOpen.username(),checkInOpen.setor().getNome(),estagiarioNome.get(),checkInOpen.setor().getId()));
        }
        if(value instanceof CheckInClosed checkInClosed){
             emitter.send(new EstagiarioEvent("close",checkInClosed.checkInOpen().username(),checkInClosed.checkInOpen().setor().getNome(),estagiarioNome.get(),checkInClosed.checkInOpen().setor().getId()));
        }
    }
    public List<EstagiarioEvent> getEstagiarioEvent(){
        var entries = redis.scan(ScanOptions.scanOptions().match("*").build()).stream().toList().stream().map(element -> redis.opsForHash().get(element,element)).toList();
        System.out.println(entries);
        return entries.stream().map(elment -> {
            System.out.println(elment.toString());
            if(elment instanceof CheckInOpen checkInOpen){
                System.out.println(checkInOpen);
                return new EstagiarioEvent("open",checkInOpen.username(),checkInOpen.setor().getNome(),repositoryEstagiarios.findNomeByMatricula(checkInOpen.username()).get(),checkInOpen.setor().getId());
            }
            return null;
        }).toList();
    }
}
