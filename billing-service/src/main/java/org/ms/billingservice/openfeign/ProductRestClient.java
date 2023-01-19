package org.ms.billingservice.openfeign;

import org.ms.billingservice.entities.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//@FeignClient(name = "INVENTORY-SERVICE", url = "https://inventory-service-ms.herokuapp.com")
@FeignClient(name = "INVENTORY-SERVICE", url = "http://localhost:8083")
public interface ProductRestClient {
    @GetMapping(path = "/api/products/{id}")
    Product getProduct(@PathVariable(name = "id") String id);
    @GetMapping(path = "/api/products")
    List<Product> allProducts();
}
