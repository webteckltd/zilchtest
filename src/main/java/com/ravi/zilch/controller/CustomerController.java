package com.ravi.zilch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ravi.zilch.repository.CustomerRepository;



@RestController("v1/customer")
public class  CustomerController  {
	
	@Autowired
	CustomerRepository customerRepo;

}
