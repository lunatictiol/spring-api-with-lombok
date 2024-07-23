package com.sab.lombokbasics.repositories;

import com.sab.lombokbasics.entities.Category;
import com.sab.lombokbasics.entities.Juice;
import com.sab.lombokbasics.repository.CategoryRepository;
import com.sab.lombokbasics.repository.JuiceRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    JuiceRepository juiceRepository;
     Juice testJuice;

    @BeforeEach
    void setUp() {
        testJuice = juiceRepository.findAll().get(0);
    }
    @Transactional
    @Test
    void testAddCategory() {
        Category category = categoryRepository.save(Category.builder()
                        .description("hbsuhbvws")
                        .build());
        testJuice.addCategory(category);

        Juice savedJuice = juiceRepository.save(testJuice);

        System.out.println(savedJuice.getCategories().size());
        assertNotNull(savedJuice.getCategories());
    }
}