package org.ms.billingservice.services;

import org.ms.billingservice.dto.ProductItemRequestDTO;
import org.ms.billingservice.dto.ProductItemResponseDTO;

import java.util.List;

public interface ProductItemService {
    ProductItemResponseDTO saveProductItem(ProductItemRequestDTO productItemRequestDTO);
    ProductItemResponseDTO getProductItem(String id);
    List<ProductItemResponseDTO> listProductItem();
    List<ProductItemResponseDTO> productItemByInvoiceId(String invoiceId);
    List<ProductItemResponseDTO> productItemByProductId(String productId);
    ProductItemResponseDTO updateProductItem(String productItemId, ProductItemRequestDTO productItemRequestDTO);
    void deleteProductItem(String productItemId);
}
