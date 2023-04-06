package com.ravi.zilch;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ravi.zilch.model.Customer;
import com.ravi.zilch.model.PaymentCard;
import com.ravi.zilch.model.PaymentTransaction;
import com.ravi.zilch.repository.CustomerRepository;

import jakarta.transaction.Transactional;

@Component
public class DataTestCommandLine implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(DataTestCommandLine.class);

	@Autowired
	CustomerRepository customerRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void run(String... args) throws Exception {

		for (int i = 0; i < 1; i++) {
			createCustomer(getCustomer());
			//Thread.sleep(500);
		}
	}

	private void createCustomer(Customer cust) {
		logger.debug("persisting custober object ");
		try {
			if(!( customerRepo.findOneByEmail(cust.getEmail().trim()).isPresent()))
			     customerRepo.saveAndFlush(cust);
		} catch (Exception e) {
			logger.error("Error Message : " + e.getMessage());
		}

	}

	private Customer getCustomer() {
		PaymentTransaction transaction = new PaymentTransaction();
		transaction.setIdempotencykey(java.util.UUID.randomUUID().toString());
		transaction.setTransactionrefernce("Test" + java.util.UUID.randomUUID().toString());
		transaction.setTransactionstatus(true);
		transaction.setTransactionamount(100);
		transaction.setTransactiondescription("Test-Desc" + java.util.UUID.randomUUID().toString());

		java.util.Random rng = new java.util.Random(); 
		long first14 = (rng.nextLong() % 100000000000000L) + 5200000000000000L;
		PaymentCard card = new PaymentCard();
		card.setCardnumber("1234567898765432");
		card.setCardbalance(500);

		card.setCvc("123");
		card.setExpirationdate(LocalDate.now().plusYears(2));
		card.addTransactions(transaction);

		
		Customer cust = new Customer();
		cust.setFirstname("Ravi-" + java.util.UUID.randomUUID().toString());
		cust.setLastname("Shukla-" + java.util.UUID.randomUUID().toString());
		//cust.setEmail("test@" + java.util.UUID.randomUUID().toString().substring(0, 10) + ".com");
		cust.setEmail("test@test.com");
		String hashPwd = passwordEncoder.encode("test1234");
		cust.setPassword(hashPwd);
		cust.setCustomerrole("SYSTEM");

		Random rand = new Random();
		int n = rand.nextInt((int) Math.pow(10, 10));
		cust.setContactnumber(String.format("%010d", n));
		cust.addCard(card);
		return cust;
	}

}
