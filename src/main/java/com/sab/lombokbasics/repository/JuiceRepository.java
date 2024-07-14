package com.sab.lombokbasics.repository;

import com.sab.lombokbasics.entities.Juice;
import com.sab.lombokbasics.model.JuiceStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JuiceRepository extends JpaRepository<Juice, UUID> {
    List<Juice> findAllByJuiceNameIsLikeIgnoreCase(String juiceName);
    List<Juice> findAllByJuiceStyle(JuiceStyle juiceStyle);
    List<Juice> findAllByJuiceNameIsLikeIgnoreCaseAndJuiceStyle(String juiceName, JuiceStyle juiceStyle);
}
