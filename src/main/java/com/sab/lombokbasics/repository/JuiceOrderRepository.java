package com.sab.lombokbasics.repository;

import com.sab.lombokbasics.entities.JuiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JuiceOrderRepository extends JpaRepository<JuiceOrder, UUID> {
}
