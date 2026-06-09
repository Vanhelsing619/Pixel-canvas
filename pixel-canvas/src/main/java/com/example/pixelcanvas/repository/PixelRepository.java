package com.example.pixelcanvas.repository;

import com.example.pixelcanvas.model.Pixel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PixelRepository extends JpaRepository<Pixel, Long> {

    Optional<Pixel> findByXAndY(int x, int y);
}
