package com.archit.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.archit.entity.Customer;
import com.archit.exception.CustomerNotFoundException;
import com.archit.service.CustomerService;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

    @Autowired
    private CustomerService customerService;

    // add mapping for GET "/customers"
    @GetMapping("/customers")
    public List<Customer> getCustomers(){
        return customerService.getCustomers();
    }

    // add mapping for GET "/customers/{customerId}"
    @GetMapping("/customers/{customerId}")
    public Customer getCustomer(@PathVariable int customerId){
        // by default Jackson returns empty body if object is null (bad customer id)
        // The correct response should be 404 error + status (not empty body with 200 OK status)
        //return customerService.getCustomer(customerId);

        Customer customer = customerService.getCustomer(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer Id not found - " + customerId);
        }
        return customer;
    }

    // add mapping for POST "/customers" (adding a new customer)
    @PostMapping("/customers")
    public Customer addCustomer(@RequestBody Customer customer) {
        // set id to 0 explictly, in case any id set in the request body
        // this will save a new customer instead of update
        customer.setId(0);
        customerService.saveCustomer(customer);
        return customer;
    }

    // add mapping for PUT "/customers" (updating a customer)
    @PutMapping("/customers")
    public Customer updateCustomer(@RequestBody Customer customer) {
        customerService.saveCustomer(customer);
        return customer;
    }

    // add mapping for DELETE "/customers/{customerId}" (delete a customer)
    @DeleteMapping("/customers/{customerId}")
    public String deleteCustomer(@PathVariable int customerId) {
        Customer customer = customerService.getCustomer(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer Id not found - " + customerId);
        }
        customerService.deleteCustomer(customerId);
        return "Deleted customer id: " + customerId;
    }

}
