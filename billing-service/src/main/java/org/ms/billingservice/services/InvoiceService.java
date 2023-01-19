package org.ms.billingservice.services;

import org.ms.billingservice.dto.InvoiceRequestDTO;
import org.ms.billingservice.dto.InvoiceResponseDTO;

import java.util.List;

public interface InvoiceService {
    InvoiceResponseDTO saveInvoice(InvoiceRequestDTO invoiceRequestDTO);
    InvoiceResponseDTO getInvoice(String invoiceId);
    List<InvoiceResponseDTO> listInvoices();
    List<InvoiceResponseDTO> invoicesByCustomerId(String customerId);
    InvoiceResponseDTO updateInvoice(String invoiceId, InvoiceRequestDTO invoiceRequestDTO);
    void delete(String invoiceId);
}
