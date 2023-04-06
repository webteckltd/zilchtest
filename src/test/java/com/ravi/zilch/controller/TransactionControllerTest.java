package com.ravi.zilch.controller;

import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ravi.zilch.model.Customer;
import com.ravi.zilch.model.PaymentCard;
import com.ravi.zilch.model.PaymentTransaction;
import com.ravi.zilch.repository.PaymentCardRepository;

import jakarta.transaction.Transactional;
import jakarta.xml.bind.DatatypeConverter;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TransactionControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(TransactionControllerTest.class);

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	PaymentCardRepository paymentCardRepo;
	
	@MockBean
	RedisConnectionFactory factory;
	
	@MockBean
	RedisConnection connection;

	@Test
	void whenWrongCredntials_thenReturnsStatus401() throws Exception {
		when(factory.getConnection()).thenReturn(connection);
		String authorizationHeader = "Basic "
				+ DatatypeConverter.printBase64Binary(("test@test.com" + ":" + "test12345").getBytes());
		Customer testCustomer = getCustomer();
		String body = objectMapper.writeValueAsString(testCustomer.getCards().get(0).getTransactions().get(0));
		mvc.perform(MockMvcRequestBuilders.post("/api/v1/transaction").header("Authorization", authorizationHeader)
				.contentType("application/json").content(body))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}
	 
	@Test
	@WithMockUser(authorities = {"TEST"} , username = "test@test123.com", password = "test1234")
	void whenWrongAuthority_thenReturnsStatus403() throws Exception {	
		when(factory.getConnection()).thenReturn(connection);
		mvc.perform(MockMvcRequestBuilders.get("/api/v1/transaction/filter?cardNumber=5108634774562539"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(username = "test@test.com", password = "test1234")
	void whenInputIsvalid_thenReturnsStatus201() throws Exception {
		when(factory.getConnection()).thenReturn(connection);
		Customer testCustomer = getCustomer();
		when(paymentCardRepo.findByCustomerCardnumber(testCustomer.getEmail(),
				testCustomer.getCards().get(0).getCardnumber())).thenReturn(testCustomer.getCards());
		when(paymentCardRepo.getReferenceById(testCustomer.getCards().get(0).getObjectID()))
				.thenReturn(testCustomer.getCards().get(0));
		
		

		String body = objectMapper.writeValueAsString(testCustomer.getCards().get(0).getTransactions().get(0));
		mvc.perform(MockMvcRequestBuilders.post("/api/v1/transaction").contentType("application/json").content(body))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	@WithMockUser(username = "test@test.com", password = "test1234")
	void whenInputIsInvalid_thenReturnsStatus400() throws Exception {
		when(factory.getConnection()).thenReturn(connection);
		Customer testCustomer = getCustomer();
		when(paymentCardRepo.findByCustomerCardnumber(testCustomer.getEmail(),
				testCustomer.getCards().get(0).getCardnumber())).thenReturn(testCustomer.getCards());
		when(paymentCardRepo.getReferenceById(testCustomer.getCards().get(0).getObjectID()))
				.thenReturn(testCustomer.getCards().get(0));
		testCustomer.getCards().get(0).getTransactions().get(0).setTransactionrefernce("");
		testCustomer.getCards().get(0).getTransactions().get(0).setTransactionamount(0);
		String body = objectMapper.writeValueAsString(testCustomer.getCards().get(0).getTransactions().get(0));

		mvc.perform(MockMvcRequestBuilders.post("/api/v1/transaction").contentType("application/json").content(body))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());

	}

	@Test
	@WithMockUser(username = "test@test.com", password = "test1234")
	void whenInsufficientFunds_thenReturnsStatus400() throws Exception {
		when(factory.getConnection()).thenReturn(connection);
		Customer testCustomer = getCustomer();
		when(paymentCardRepo.findByCustomerCardnumber(testCustomer.getEmail(),
				testCustomer.getCards().get(0).getCardnumber())).thenReturn(testCustomer.getCards());
		when(paymentCardRepo.getReferenceById(testCustomer.getCards().get(0).getObjectID()))
				.thenReturn(testCustomer.getCards().get(0));

		testCustomer.getCards().get(0).setCardbalance(50);
		String body = objectMapper.writeValueAsString(testCustomer.getCards().get(0).getTransactions().get(0));
		mvc.perform(MockMvcRequestBuilders.post("/api/v1/transaction").contentType("application/json").content(body))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
		
	}

	@Test
	@WithMockUser(username = "test@test123.com", password = "test1234")
	void whenCardOwnerisDifferent_thenReturnsStatus400() throws Exception {
		when(factory.getConnection()).thenReturn(connection);
		Customer testCustomer = getCustomer();
		when(paymentCardRepo.findByCustomerCardnumber(testCustomer.getEmail(),
				testCustomer.getCards().get(0).getCardnumber())).thenReturn(testCustomer.getCards());
		when(paymentCardRepo.getReferenceById(testCustomer.getCards().get(0).getObjectID()))
				.thenReturn(testCustomer.getCards().get(0));

		String body = objectMapper.writeValueAsString(testCustomer.getCards().get(0).getTransactions().get(0));
		
		mvc.perform(MockMvcRequestBuilders.post("/api/v1/transaction").contentType("application/json").content(body))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	private Customer getCustomer() {
		PaymentTransaction transaction = new PaymentTransaction();
		transaction.setIdempotencykey(java.util.UUID.randomUUID().toString());
		transaction.setTransactionrefernce("Test" + java.util.UUID.randomUUID().toString());
		transaction.setTransactionstatus(false);
		transaction.setTransactionamount(100);
		transaction.setTransactiondescription("Test-Desc" + java.util.UUID.randomUUID().toString());

		java.util.Random rng = new java.util.Random(); // Provide a seed if you want the same ones every time
		long first14 = (rng.nextLong() % 100000000000000L) + 5200000000000000L;
		PaymentCard card = new PaymentCard();
		card.setCardnumber(Long.toString(first14));
		card.setCardbalance(500);

		card.setCvc("123");
		card.setExpirationdate(LocalDate.now().plusYears(2));
		card.addTransactions(transaction);

		Customer cust = new Customer();
		cust.setFirstname("Ravi-" + java.util.UUID.randomUUID().toString());
		cust.setLastname("Shukla-" + java.util.UUID.randomUUID().toString());
		cust.setEmail("test@test.com");
		//String hashPwd = passwordEncoder.encode("test1234");
		cust.setPassword("hashPwd");
		 cust.setPassword("test1234");
		cust.setCustomerrole("SYSTEM");

		Random rand = new Random();
		int n = rand.nextInt((int) Math.pow(10, 10));
		cust.setContactnumber(String.format("%010d", n));
		cust.addCard(card);
		return cust;
	}
	
	

}
