package org.ms.inventoryservice.services;

import org.ms.inventoryservice.dto.ProductRequestDTO;
import org.ms.inventoryservice.dto.ProductResponseDTO;
import org.ms.inventoryservice.entities.Product;
import org.ms.inventoryservice.mappers.ProductMapper;
import org.ms.inventoryservice.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper){
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponseDTO saveProduct(ProductRequestDTO productRequestDTO) {
        if (productRequestDTO.getPrice().equals(BigDecimal.valueOf(0)) || productRequestDTO.getName().equals(""))
            return null;
        Product product = productMapper.ProductRequestDTOToProduct(productRequestDTO);
        product.setId(UUID.randomUUID().toString());
        Product productSave = productRepository.save(product);
        return productMapper.ProductToProductResponseDTO(productSave);
    }

    @Override
    public ProductResponseDTO getProduct(String id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null)
            return null;
        return productMapper.ProductToProductResponseDTO(product);
    }

    @Override
    public ProductResponseDTO updateProduct(String id, ProductRequestDTO productRequestDTO) {
        if (productRequestDTO.getPrice().equals(BigDecimal.valueOf(0)) || productRequestDTO.getName().equals(""))
            return null;
        if(productRepository.findById(id).isPresent()){
            Product product = productMapper.ProductRequestDTOToProduct(productRequestDTO);
            product.setId(id);
            Product productUpdate = productRepository.save(product);
            return productMapper.ProductToProductResponseDTO(productUpdate);
        }
        return null;
    }

    @Override
    public List<ProductResponseDTO> listProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                        .map(productMapper::ProductToProductResponseDTO)
                        .collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(String id) {
        if(productRepository.findById(id).isPresent())
            productRepository.deleteById(id);
    }
}
