package org.ms.customerservice;

import org.ms.customerservice.dto.CustomerRequestDTO;
import org.ms.customerservice.services.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CustomerService customerService){
        return args -> {
            customerService.save(new CustomerRequestDTO("Browndon","browndon@gmail.com"));
            customerService.save(new CustomerRequestDTO("BK","bk@gmail.com"));
            customerService.save(new CustomerRequestDTO("BKO","bko@gmail.com"));
            customerService.save(new CustomerRequestDTO("Brown","brown@gmail.com"));
            customerService.save(new CustomerRequestDTO("Kuimo","Kuimo@gmail.com"));
        };
    }

}
