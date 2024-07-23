package com.sab.lombokbasics.repositories;

import com.sab.lombokbasics.entities.Customer;
import com.sab.lombokbasics.entities.Juice;
import com.sab.lombokbasics.entities.JuiceOrder;
import com.sab.lombokbasics.repository.CustomerRepository;
import com.sab.lombokbasics.repository.JuiceOrderRepository;
import com.sab.lombokbasics.repository.JuiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class JuiceOrderRepositoryTest {
   @Autowired
   JuiceOrderRepository juiceOrderRepository;
   @Autowired
   CustomerRepository customerRepository;
   @Autowired
   JuiceRepository juiceRepository;

   Customer testCustomer;
   Juice testJuice;
    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.findAll().get(0);
        testJuice = juiceRepository.findAll().get(0);

    }
    @Transactional
    @Test
    void testJuiceOrderRepository() {
        JuiceOrder juiceOrder = JuiceOrder.builder()
                .customerRef("test ref")
                .customer(testCustomer).build();
        JuiceOrder savedJuiceOrder = juiceOrderRepository.saveAndFlush(juiceOrder);
        assertNotNull(savedJuiceOrder);
   }
}