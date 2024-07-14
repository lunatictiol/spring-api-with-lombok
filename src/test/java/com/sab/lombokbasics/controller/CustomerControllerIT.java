package com.sab.lombokbasics.controller;

import com.sab.lombokbasics.entities.Customer;
import com.sab.lombokbasics.model.CustomerDTO;
import com.sab.lombokbasics.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@RequiredArgsConstructor
class CustomerControllerIT {
     @Autowired
     CustomerController customerController;
    @Autowired
     CustomerRepository customerRepository;



    @Test
    void getAllCustomers() {
        List<CustomerDTO> customerDTOList = customerController.listAllCustomers();
        assertThat(customerDTOList.size()).isEqualTo(3);
    }
    @Rollback
    @Transactional
    @Test
    void getAllCustomersNotFound() {
        customerRepository.deleteAll();
        List<CustomerDTO> customerDTOList = customerController.listAllCustomers();
        assertThat(customerDTOList.size()).isEqualTo(0);
    }

    @Test
    void getCustomerById() {
        Customer cus = customerRepository.findAll().get(0);

        CustomerDTO customerDTO = customerController.getCustomerById(cus.getId());

        assertThat(customerDTO).isNotNull();
    }

    @Test
    void customerNotFoundException(){
        assertThrows(NotFoundException.class,()->{
            customerController.getCustomerById(UUID.randomUUID());
        });
    }
}