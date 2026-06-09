package com.example.pixelcanvas.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PresenceListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final AtomicInteger online = new AtomicInteger(0);

    public PresenceListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void onConnect(SessionConnectedEvent event) {
        broadcast(online.incrementAndGet());
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent event) {
        broadcast(Math.max(0, online.decrementAndGet()));
    }

    private void broadcast(int count) {
        messagingTemplate.convertAndSend("/topic/presence", Map.of("online", count));
    }
}
