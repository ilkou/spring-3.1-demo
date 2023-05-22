package com.ouklich.demo.productivity;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
public class ProductivityApplication {

	@Bean
	public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
		return new ObservedAspect(observationRegistry);
	}

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


	@Observed(name = "getCustomers")
	@GetMapping("/customers")
	Iterable<Customer> getCustomers() {
		return customerRepository.findAll();
	}

	@GetMapping("/customers/{id}")
	Customer getCustomer(@PathVariable Integer id) {
		return customerRepository.findById(id).orElseThrow();
	}
}
