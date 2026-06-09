package com.example.pixelcanvas.controller;

import com.example.pixelcanvas.dto.PixelMessage;
import com.example.pixelcanvas.service.CanvasService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class CanvasWebSocketController {

    private final CanvasService canvasService;
    private final SimpMessagingTemplate messagingTemplate;

    public CanvasWebSocketController(CanvasService canvasService,
                                     SimpMessagingTemplate messagingTemplate) {
        this.canvasService = canvasService;
        this.messagingTemplate = messagingTemplate;
    }

    /** Client sends paint requests to /app/paint; valid ones are broadcast to /topic/pixels. */
    @MessageMapping("/paint")
    public void paint(PixelMessage message) {
        PixelMessage applied = canvasService.paint(message);
        if (applied != null) {
            messagingTemplate.convertAndSend("/topic/pixels", applied);
        }
    }
}
