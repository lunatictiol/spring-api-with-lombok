package com.sab.lombokbasics.repository;

import com.sab.lombokbasics.entities.Juice;
import com.sab.lombokbasics.model.JuiceStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JuiceRepository extends JpaRepository<Juice, UUID> {
    Page<Juice> findAllByJuiceNameIsLikeIgnoreCase(String juiceName, Pageable pageable);
    Page<Juice> findAllByJuiceStyle(JuiceStyle juiceStyle, Pageable pageable);
    Page<Juice> findAllByJuiceNameIsLikeIgnoreCaseAndJuiceStyle(String juiceName, JuiceStyle juiceStyle, Pageable pageable);
}
