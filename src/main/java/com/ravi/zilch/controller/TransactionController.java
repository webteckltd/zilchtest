package com.ravi.zilch.controller;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ravi.zilch.exception.ErrorResponse;
import com.ravi.zilch.exception.ZilchAppException;
import com.ravi.zilch.model.PaymentTransaction;
import com.ravi.zilch.service.TransactionService;


import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/transaction")
public class TransactionController {

	private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);


	@Autowired
	TransactionService transactionService;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@GetMapping("/{transactionId}")
	public EntityModel<PaymentTransaction> getTransactionDetail(Principal principal,
			@PathVariable(required = true) String transactionId) {
		try {


			PaymentTransaction transactions = transactionService.getTransactionDetail(principal.getName().trim(), transactionId);

			EntityModel<PaymentTransaction> transaction = EntityModel.of(transactions);
			WebMvcLinkBuilder link = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).loadAllTransactions(principal));
			transaction.add(link.withRel("all-transactions"));

			WebMvcLinkBuilder filterlink = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).searchNFilterTransactions(
							transactions.getPaymentCard().getCardnumber(), transactions.getTransactionrefernce(),
							transactions.getCreateDate().toLocalDate(), LocalDate.now().format(formatter)));
			transaction.add(filterlink.withRel("filter-transactions"));
			return transaction;

		} catch (ZilchAppException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			ErrorResponse error = new ErrorResponse();
			logger.error("Inside ManageTransactionService -> getTransactionDetail() Exception Happend", e);
			error.setErrorMessage(e.getMessage());
			error.setTimestamp(LocalDateTime.now());
			throw new ZilchAppException(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping
	public List<PaymentTransaction> loadAllTransactions(Principal principal) {
		return transactionService.loadAllTransactions(principal.getName().trim());
	}
	
	
	@GetMapping(path = "/filter")
	public ResponseEntity<Object> searchNFilterTransactions( @RequestParam(defaultValue = "") String cardNumber,
            @RequestParam(defaultValue = "") String referenceInclude,
            @RequestParam(defaultValue = "1980-01-01")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(defaultValue = "") String dateTo) {
		try {
			
			List<PaymentTransaction> transaction  = transactionService.searchNFilterTransactions(cardNumber, referenceInclude, dateFrom, dateTo);
				return ResponseEntity.ok(transaction);
			
		} catch (ZilchAppException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			ErrorResponse error = new ErrorResponse();
			logger.error("Inside ManageTransactionService -> loadTransactions() Exception Happend", e);
			error.setErrorMessage(e.getMessage());
			error.setTimestamp(LocalDateTime.now());
			throw new ZilchAppException(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<Object> doPayment(Principal principal, @RequestBody @Valid PaymentTransaction payment,
			BindingResult bindingResult) {	
		
		try {
			if (bindingResult.hasErrors()) {
				ErrorResponse error = new ErrorResponse();
				error.setTimestamp(LocalDateTime.now());
				error.setErrorMessage(bindingResult.getFieldError().getDefaultMessage());
				throw new ZilchAppException(error, HttpStatus.BAD_REQUEST);
			}

			PaymentTransaction transaction = transactionService.doPayment(principal.getName().trim(), payment);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{transactionId}")
					.buildAndExpand(transaction.getObjectID())
					.toUri();  
			
			return ResponseEntity.created(location).build();
			
		} catch (ZilchAppException e) {
			throw e;
		}

		catch (Exception e) {
			e.printStackTrace();
			ErrorResponse error = new ErrorResponse();
			logger.error("Inside ManageTransactionService->managePayment() Exception Happend", e);
			error.setErrorMessage(e.getMessage());
			error.setTimestamp(LocalDateTime.now());
			throw new ZilchAppException(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
