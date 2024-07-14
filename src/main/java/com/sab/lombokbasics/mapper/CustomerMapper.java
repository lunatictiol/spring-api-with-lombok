package com.sab.lombokbasics.mapper;

import com.sab.lombokbasics.entities.Customer;
import com.sab.lombokbasics.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

   Customer customerDTOToCustomer(CustomerDTO customerDTO);
   CustomerDTO customerToCustomerDTO(Customer customer);
}
