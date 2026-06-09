package com.example.pixelcanvas.service;

import com.example.pixelcanvas.dto.PixelMessage;
import com.example.pixelcanvas.model.Pixel;
import com.example.pixelcanvas.repository.PixelRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CanvasService {

    /** Square grid dimension (SIZE x SIZE cells). */
    public static final int SIZE = 40;

    /** Allowed colors. Painting with anything outside this palette is rejected. */
    public static final List<String> PALETTE = List.of(
            "#FFFFFF", "#D4D7D9", "#898D90", "#000000",
            "#FF99AA", "#E50000", "#E59500", "#A06A42",
            "#E5D900", "#94E044", "#02BE01", "#00D3DD",
            "#0083C7", "#0000EA", "#820080", "#CF6EE4"
    );

    private final PixelRepository repository;
    private final String[][] board = new String[SIZE][SIZE];
    private final Object lock = new Object();

    public CanvasService(PixelRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void loadBoard() {
        for (Pixel p : repository.findAll()) {
            if (inBounds(p.getX(), p.getY())) {
                board[p.getY()][p.getX()] = p.getColor();
            }
        }
    }

    /** Returns only the painted cells, so a fresh canvas sends almost nothing. */
    public List<PixelMessage> snapshot() {
        List<PixelMessage> pixels = new ArrayList<>();
        synchronized (lock) {
            for (int y = 0; y < SIZE; y++) {
                for (int x = 0; x < SIZE; x++) {
                    if (board[y][x] != null) {
                        pixels.add(new PixelMessage(x, y, board[y][x]));
                    }
                }
            }
        }
        return pixels;
    }

    /** Validates and applies a paint. Returns the applied message, or null if rejected. */
    public PixelMessage paint(PixelMessage msg) {
        if (msg == null || !inBounds(msg.getX(), msg.getY()) || !PALETTE.contains(msg.getColor())) {
            return null;
        }
        synchronized (lock) {
            board[msg.getY()][msg.getX()] = msg.getColor();
            Pixel pixel = repository.findByXAndY(msg.getX(), msg.getY()).orElseGet(Pixel::new);
            pixel.setX(msg.getX());
            pixel.setY(msg.getY());
            pixel.setColor(msg.getColor());
            repository.save(pixel);
        }
        return new PixelMessage(msg.getX(), msg.getY(), msg.getColor());
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }

    public int getSize() {
        return SIZE;
    }

    public List<String> getPalette() {
        return PALETTE;
    }
}
