package org.ms.billingservice.web;

import org.ms.billingservice.dto.InvoiceRequestDTO;
import org.ms.billingservice.dto.InvoiceResponseDTO;
import org.ms.billingservice.services.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/api")
public class InvoiceRestAPI {
    private final InvoiceService invoiceService;

    public InvoiceRestAPI(InvoiceService invoiceService){
        this.invoiceService = invoiceService;
    }

    @GetMapping(path = "/invoices/{id}")
    public InvoiceResponseDTO getInvoice(@PathVariable(name = "id") String invoiceId){
        return invoiceService.getInvoice(invoiceId);
    }

    @GetMapping(path = "/invoices")
    public List<InvoiceResponseDTO> listInvoices(){
        return invoiceService.listInvoices();
    }

    @GetMapping(path = "/invoices/ByCustomer/{customerId}")
    public List<InvoiceResponseDTO> getInvoiceByCustomer(@PathVariable String customerId){
        return invoiceService.invoicesByCustomerId(customerId);
    }

    @PostMapping(path = "/invoices")
    public InvoiceResponseDTO save(@RequestBody InvoiceRequestDTO invoiceRequestDTO){
        return invoiceService.saveInvoice(invoiceRequestDTO);
    }

    @PutMapping(path = "/invoices/{id}")
    public InvoiceResponseDTO updateInvoice(@PathVariable String id, @RequestBody InvoiceRequestDTO invoiceRequestDTO){
        return invoiceService.updateInvoice(id, invoiceRequestDTO);
    }

    @DeleteMapping(path = "/invoices/{id}")
    public void delete(@PathVariable String id){
        invoiceService.delete(id);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
