package com.sab.lombokbasics.services;

import com.sab.lombokbasics.mapper.CustomerMapper;
import com.sab.lombokbasics.model.CustomerDTO;
import com.sab.lombokbasics.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {
   private final CustomerRepository customerRepository;
   private final CustomerMapper customerMapper;

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID uuid) {
        return Optional.ofNullable(
                customerMapper.customerToCustomerDTO(
                        customerRepository.findById(uuid).orElse(null)
                )
        );

    }


    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDTO).toList();
    }
    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customerDto) {
        return null;
    }



    @Override
    public void updateCustomer(UUID id, CustomerDTO customerDto) {

    }

    @Override
    public void deleteCustomer(UUID id) {

    }

    @Override
    public void patchCustomer(UUID id, CustomerDTO customerDto) {

    }
}
