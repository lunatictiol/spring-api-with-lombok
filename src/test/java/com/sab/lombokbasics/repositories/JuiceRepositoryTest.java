package com.sab.lombokbasics.repositories;

import com.sab.lombokbasics.bootstrap.DataBootstrap;
import com.sab.lombokbasics.entities.Juice;
import com.sab.lombokbasics.model.JuiceStyle;
import com.sab.lombokbasics.repository.JuiceRepository;
import com.sab.lombokbasics.services.JuiceCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({DataBootstrap.class, JuiceCsvServiceImpl.class})
public class JuiceRepositoryTest {
    @Autowired
    private JuiceRepository juiceRepository;

    @Test
    void saveJuiceTest() {
        Juice juice = juiceRepository.save(
                Juice.builder()
                        .juiceName("test name")
                        .upc("test")
                        .juiceStyle(JuiceStyle.ALE)
                        .price(BigDecimal.valueOf(10.0))
                        .build()
        );
        juiceRepository.flush();
        assertThat(juice.getId()).isNotNull();
        assertThat(juice.getJuiceName()).isNotNull();
    }
    @Test
    void nameTooLongTest() {
        assertThrows(ConstraintViolationException.class, () -> {
            Juice juice = juiceRepository.save(
                    Juice.builder()
                            .juiceName("test name 1234512345123451234512345123451234512345123451234512345123451234512345")
                            .upc("test")
                            .price(BigDecimal.valueOf(10.0))
                            .build()
            );
            juiceRepository.flush();
        });


    }
    @Test
    void getJuicesByNameTest(){
        List<Juice> juices = juiceRepository.findAllByJuiceNameIsLikeIgnoreCase("%IPA%");
        assertThat(juices.size()).isEqualTo(336);

    }
    @Test
    void getJuicesByStyleTest(){
        List<Juice> juices = juiceRepository.findAllByJuiceStyle(JuiceStyle.ALE);
        assertThat(juices.size()).isEqualTo(400);

    }
}
