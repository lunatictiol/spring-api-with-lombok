package com.sab.lombokbasics.services;

import com.sab.lombokbasics.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    Optional<CustomerDTO> getCustomerById(UUID uuid);
    CustomerDTO saveNewCustomer(CustomerDTO customerDto);
    List<CustomerDTO> getAllCustomers();
    void updateCustomer(UUID id, CustomerDTO customerDto);
    void deleteCustomer(UUID id);
    void patchCustomer(UUID id, CustomerDTO customerDto);

}