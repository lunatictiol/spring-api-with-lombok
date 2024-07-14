package com.sab.lombokbasics.services;

import com.sab.lombokbasics.model.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
@Service

public class CustomerServiceImpl implements CustomerService {
    private Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        CustomerDTO customerDTO1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer 1")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customerDTO2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer 2")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customerDTO3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer 3")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        customerMap = new HashMap<>();
        customerMap.put(customerDTO1.getId(), customerDTO1);
        customerMap.put(customerDTO2.getId(), customerDTO2);
        customerMap.put(customerDTO3.getId(), customerDTO3);
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID uuid) {
        return Optional.of(customerMap.get(uuid));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customerDto) {

        CustomerDTO savedCustomerDTO = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .updateDate(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .name(customerDto.getName())
                .build();

        customerMap.put(savedCustomerDTO.getId(), savedCustomerDTO);

        return savedCustomerDTO;
    }


    @Override
    public List<CustomerDTO> getAllCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public void updateCustomer(UUID id, CustomerDTO customerDto) {
        CustomerDTO existingCustomerDTO = customerMap.get(id);
        existingCustomerDTO.setName(customerDto.getName());



        customerMap.put(existingCustomerDTO.getId(), existingCustomerDTO);

    }

    @Override
    public void deleteCustomer(UUID id) {
        customerMap.remove(id);
    }

    @Override
    public void patchCustomer(UUID id, CustomerDTO customerDto) {
        CustomerDTO existingCustomerDTO = customerMap.get(id);
        if (StringUtils.hasText(customerDto.getName())){
            existingCustomerDTO.setName(customerDto.getName());
        }
    }

}
