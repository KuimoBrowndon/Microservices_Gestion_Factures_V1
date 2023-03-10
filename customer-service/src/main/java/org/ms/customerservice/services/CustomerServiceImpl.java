package org.ms.customerservice.services;

import org.ms.customerservice.dto.CustomerRequestDTO;
import org.ms.customerservice.dto.CustomerResponseDTO;
import org.ms.customerservice.entities.Customer;
import org.ms.customerservice.mappers.CustomerMapper;
import org.ms.customerservice.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerResponseDTO save(CustomerRequestDTO customerRequestDTO) {
        if (customerRequestDTO.getEmail().equals("") || customerRequestDTO.getName().equals("")) return null;
        Customer customer = customerMapper.customerRequestDTOToCustomer(customerRequestDTO);
        customer.setId(UUID.randomUUID().toString());
        Customer saveCustomer=customerRepository.save(customer);
        return customerMapper.customerToCustomerResponseDTO(saveCustomer);
    }

    @Override
    public CustomerResponseDTO getCustomer(String id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        return customerMapper.customerToCustomerResponseDTO(customer);
    }

    @Override
    public CustomerResponseDTO update(String id, CustomerRequestDTO customerRequestDTO) {
        if (customerRequestDTO.getEmail().equals("") || customerRequestDTO.getName().equals("")) return null;
        Customer updatedCustomer = null;
        if(customerRepository.findById(id).isPresent()){
            Customer customer = customerMapper.customerRequestDTOToCustomer(customerRequestDTO);
            customer.setId(id);
            updatedCustomer = customerRepository.save(customer);
        }
        return customerMapper.customerToCustomerResponseDTO(updatedCustomer);
    }

    @Override
    public List<CustomerResponseDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customerMapper::customerToCustomerResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id){
        if(customerRepository.findById(id).isPresent())
            customerRepository.deleteById(id);
    }
}
