package org.ms.billingservice.services;

import org.ms.billingservice.dto.InvoiceRequestDTO;
import org.ms.billingservice.dto.InvoiceResponseDTO;
import org.ms.billingservice.dto.ProductItemResponseDTO;
import org.ms.billingservice.entities.Customer;
import org.ms.billingservice.entities.Invoice;
import org.ms.billingservice.entities.ProductItem;
import org.ms.billingservice.exceptions.CustomerNotFoundException;
import org.ms.billingservice.mappers.InvoiceMapper;
import org.ms.billingservice.mappers.ProductItemMapper;
import org.ms.billingservice.openfeign.CustomerRestClient;
import org.ms.billingservice.repositories.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final CustomerRestClient customerRestClient;
    private final ProductItemService productItemService;
    private final ProductItemMapper productItemMapper;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper, customerRestClient customerRestClient, ProductItemService productItemService, ProductItemMapper productItemMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.customerRestClient = customerRestClient;
        this.productItemService = productItemService;
        this.productItemMapper = productItemMapper;
    }

    @Override
    public InvoiceResponseDTO saveInvoice(InvoiceRequestDTO invoiceRequestDTO) {
        if (invoiceRequestDTO.getAmount().equals(BigDecimal.valueOf(0)) || invoiceRequestDTO.getCustomerId().equals(""))
            throw new CustomerNotFoundException("Vous n'avez pas envoyé toutes les données requises");
        Customer customer = customerRestClient.getCustomer(invoiceRequestDTO.getCustomerId());
        if (customer == null)
            throw new CustomerNotFoundException("Ce client n'existe pas");
        Invoice invoice = invoiceMapper.InvoiceRequestDTOToInvoice(invoiceRequestDTO);
        invoice.setId(UUID.randomUUID().toString());
        invoice.setDate(new Date());
        Invoice invoiceSave = invoiceRepository.save(invoice);
        invoiceSave.setCustomer(customer);
        return invoiceMapper.InvoiceToInvoiceResponseDTO(invoiceSave);
    }

    @Override
    public InvoiceResponseDTO getInvoice(String invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);
        if (invoice == null)
            throw new CustomerNotFoundException("Cette facture n'existe pas");
        Customer customer = customerRestClient.getCustomer(invoice.getCustomerId());
        invoice.setCustomer(customer);
        List<ProductItemResponseDTO> productItemResponseDTOS = productItemService.productItemByInvoiceId(invoiceId);
        List<ProductItem> productItems = productItemMapper.LProductItemResponseDTOToLProductItem(productItemResponseDTOS);
        invoice.setProductItems(productItems);
        return invoiceMapper.InvoiceToInvoiceResponseDTO(invoice);
    }

    @Override
    public List<InvoiceResponseDTO> listInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        for (Invoice invoice:invoices){
            Customer customer = customerRestClient.getCustomer(invoice.getCustomerId());
            invoice.setCustomer(customer);
            List<ProductItemResponseDTO> productItemResponseDTOS = productItemService.productItemByInvoiceId(invoice.getId());
            List<ProductItem> productItems = productItemMapper.LProductItemResponseDTOToLProductItem(productItemResponseDTOS);
            invoice.setProductItems(productItems);
        }
        return invoices.stream()
                .map(invoiceMapper::InvoiceToInvoiceResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceResponseDTO> invoicesByCustomerId(String customerId) {
        Customer customer = customerRestClient.getCustomer(customerId);
        if (customer == null)
            throw new CustomerNotFoundException("Ce client n'existe pas");
        else if (invoiceRepository.findByCustomerId(customerId).isEmpty())
            throw new CustomerNotFoundException("Ce client existe mais n'a pas de facture");
        else{
            List<Invoice> invoices = invoiceRepository.findByCustomerId(customerId);
            for (Invoice invoice:invoices) {
                invoice.setCustomer(customer);
                List<ProductItemResponseDTO> productItemResponseDTOS = productItemService.productItemByInvoiceId(invoice.getId());
                List<ProductItem> productItems = productItemMapper.LProductItemResponseDTOToLProductItem(productItemResponseDTOS);
                invoice.setProductItems(productItems);
            }
            return invoices.stream()
                    .map(invoiceMapper::InvoiceToInvoiceResponseDTO)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public InvoiceResponseDTO updateInvoice(String invoiceId, InvoiceRequestDTO invoiceRequestDTO) {
        if (invoiceRequestDTO.getAmount().equals(BigDecimal.valueOf(0)) || invoiceRequestDTO.getCustomerId().equals(""))
            throw new CustomerNotFoundException("Vous n'avez pas envoyé toutes les données requises");
        Invoice invoiceUpdate = invoiceRepository.findById(invoiceId).orElse(null);
        if (invoiceUpdate == null)
            throw new CustomerNotFoundException("Cette facture n'existe pas");
        Customer customer = customerRestClient.getCustomer(invoiceRequestDTO.getCustomerId());
        if (customer == null)
            throw new CustomerNotFoundException("Ce client n'existe pas");
        Invoice invoice = invoiceMapper.InvoiceRequestDTOToInvoice(invoiceRequestDTO);
        invoice.setId(invoiceId);
        invoice.setDate(new Date());
        invoiceUpdate = invoiceRepository.save(invoice);
        invoiceUpdate.setCustomer(customer);
        return invoiceMapper.InvoiceToInvoiceResponseDTO(invoiceUpdate);
    }

    @Override
    public void delete(String invoiceId){
        if(invoiceRepository.findById(invoiceId).isPresent()) {
            List<ProductItemResponseDTO> productItemResponseDTOS = productItemService.productItemByInvoiceId(invoiceId);
            for (ProductItemResponseDTO productItemResponseDTO:productItemResponseDTOS)
                productItemService.deleteProductItem(productItemResponseDTO.getId());
            invoiceRepository.deleteById(invoiceId);
        }
        else throw new CustomerNotFoundException("Cette facture n'existe pas");
    }
}
