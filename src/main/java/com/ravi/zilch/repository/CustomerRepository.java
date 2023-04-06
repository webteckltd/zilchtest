package com.ravi.zilch.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ravi.zilch.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
	
	Optional<Customer> findOneByEmail(String emailAddres);

}
