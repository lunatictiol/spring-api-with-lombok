package com.sab.lombokbasics.repositories;

import com.sab.lombokbasics.entities.Customer;
import com.sab.lombokbasics.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CustomerRepositoryTest {
@Autowired
    private CustomerRepository customerRepository;


@Test
    void saveCustomerTest() {
    Customer customer = customerRepository.save(
            Customer.builder()
                    .name("test user")
                    .build());
    assertThat(customer.getId()).isNotNull();
    assertThat(customer.getName()).isNotNull();
}

}
