package com.example.pixelcanvas;

import com.example.pixelcanvas.dto.PixelMessage;
import com.example.pixelcanvas.service.CanvasService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class PixelCanvasApplicationTests {

    @Autowired
    private CanvasService canvasService;

    @Test
    void contextLoads() {
        assertNotNull(canvasService);
    }

    @Test
    void validPaintIsApplied() {
        PixelMessage applied = canvasService.paint(new PixelMessage(1, 1, "#E50000"));
        assertNotNull(applied);
        assertEquals("#E50000", applied.getColor());
    }

    @Test
    void outOfBoundsOrBadColorIsRejected() {
        assertNull(canvasService.paint(new PixelMessage(-1, 0, "#E50000")));
        assertNull(canvasService.paint(new PixelMessage(0, 0, "#123456"))); // not in palette
    }
}
