package com.ouklich.demo.productivity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ProductivityApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductivityApplication.class, args);
	}

}

record Customer(@Id Integer id, String name, String email) {}

interface CustomerRepository extends CrudRepository<Customer, Integer> {}

@RestController
class CustomerController {

	private final CustomerRepository customerRepository;

	CustomerController(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}


	@GetMapping("/customers")
	Iterable<Customer> getCustomers() {
		return customerRepository.findAll();
	}

	@GetMapping("/customers/{id}")
	Customer getCustomer(@PathVariable Integer id) {
		return customerRepository.findById(id).orElseThrow();
	}
}
