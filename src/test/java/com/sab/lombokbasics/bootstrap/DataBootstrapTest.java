package com.sab.lombokbasics.bootstrap;

import com.sab.lombokbasics.repository.CustomerRepository;
import com.sab.lombokbasics.repository.JuiceRepository;
import com.sab.lombokbasics.services.JuiceCsvService;
import com.sab.lombokbasics.services.JuiceCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JuiceCsvServiceImpl.class)
class DataBootstrapTest {
    @Autowired
    JuiceCsvService juiceCsvService;
     @Autowired
     JuiceRepository juiceRepository;
     @Autowired
     CustomerRepository customerRepository;


     DataBootstrap dataBootstrap;

     @BeforeEach
     void setUp() {
         dataBootstrap = new DataBootstrap(juiceRepository, customerRepository,juiceCsvService);
     }
    @Test
    void testRun() throws Exception {
         dataBootstrap.run(null);

         assertThat(juiceRepository.count()).isEqualTo(2410);
         assertThat(customerRepository.count()).isEqualTo(3);
    }
}