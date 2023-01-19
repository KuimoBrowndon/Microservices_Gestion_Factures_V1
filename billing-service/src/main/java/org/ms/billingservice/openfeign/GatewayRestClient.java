package org.ms.billingservice.openfeign;

import org.ms.billingservice.entities.Customer;
import org.ms.billingservice.entities.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//@FeignClient(name = "GATEWAY-SERVICE", url = "https://gateway-service-ms.herokuapp.com/")
@FeignClient(name = "GATEWAY-SERVICE", url = "http://localhost:8081")
public interface GatewayRestClient {
    @GetMapping(path = "CUSTOMER-SERVICE/api/customers/{id}")
    Customer getCustomer(@PathVariable(name = "id") String id);
    @GetMapping(path = "CUSTOMER-SERVICE/api/customers")
    List<Customer> allCustomers();
    @GetMapping(path = "INVENTORY-SERVICE/api/products/{id}")
    Product getProduct(@PathVariable(name = "id") String id);
    @GetMapping(path = "INVENTORY-SERVICE/api/products")
    List<Product> allProducts();
}
