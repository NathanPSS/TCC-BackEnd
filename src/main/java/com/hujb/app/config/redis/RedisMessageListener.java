package com.hujb.app.config.redis;

import com.hujb.app.usuarios.estagiarios.services.EstagiarioMessageService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.FluxSink;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@Component
public class RedisMessageListener implements MessageListener {
    private FluxSink<String> handler;

    private  SseEmitter emitter = null;

    private final EstagiarioMessageService estagiarioMessageService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String body = new String(message.getBody());
        String channel = new String(message.getChannel());
        System.out.println(body);
        System.out.println(channel);
        String expiredKey = channel.split(":")[1];
        if (handler != null) {
            /*
             * null check is required to avoid NPE if a message is received
             * before any subscription occurs since handler is instantiated
             * lazily when the first subscription is requested
             */
            handler.next(Arrays.toString(message.getChannel()));
        }
        try {
            if(body.equals("hset")){
                estagiarioMessageService.parseAndSendEvent(message,this.emitter);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //implemented logic to discard task
    }
    public RedisMessageListener(EstagiarioMessageService estagiarioMessageService) {
        this.estagiarioMessageService = estagiarioMessageService;
    }

   public SseEmitter emitter(){
       emitter = new SseEmitter(-1L);
       emitter.onCompletion(() -> this.emitter = new SseEmitter(-1L));
        return this.emitter;
   }
    }

