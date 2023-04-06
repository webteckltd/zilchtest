package com.ravi.zilch.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.ravi.zilch.exception.ErrorResponse;
import com.ravi.zilch.exception.ZilchAppException;
import com.ravi.zilch.model.PaymentCard;
import com.ravi.zilch.model.PaymentTransaction;
import com.ravi.zilch.repository.PaymentCardRepository;
import com.ravi.zilch.repository.TransactionRepository;

@Component
public class TransactionService {
	
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

	@Autowired
	private PaymentCardRepository paymentCardRepo;
	
	@Autowired
	private TransactionRepository transactionRepo;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	
	@Cacheable(cacheNames = "transactionStore", key = "#transactionId") 
	//@Cacheable(value = "users", key = "#transactionId", cacheManager ="zilchRedisCacheManager")
	public  PaymentTransaction   getTransactionDetail(String principal, String transactionId) {
		try {
			
			
			Optional<PaymentTransaction> transactions = transactionRepo.findOneTransactionForUser(principal, transactionId.trim());
			return transactions.orElseThrow();
			
		} catch (NoSuchElementException e) {
			ErrorResponse error = new ErrorResponse();
			error.setErrorMessage(e.getMessage());
			error.setTimestamp(LocalDateTime.now());
			throw new ZilchAppException(error, HttpStatus.BAD_REQUEST);
		}catch (Exception e) {
			e.printStackTrace();
			ErrorResponse error = new ErrorResponse();
			logger.error("Inside ManageTransactionService -> getTransactionDetail() Exception Happend", e);
			error.setErrorMessage(e.getMessage());
			error.setTimestamp(LocalDateTime.now());
			throw new ZilchAppException(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	public List<PaymentTransaction> loadAllTransactions(String principal) {
		return transactionRepo.findAllWithCustomer(principal);
	}
	
	
	
	public List<PaymentTransaction> searchNFilterTransactions(String cardNumber,String referenceInclude,LocalDate dateFrom,String dateTo) {
		try {
			

			LocalDate endDate;
			try {
				endDate = LocalDate.parse(dateTo);
			} catch (Exception e) {
				endDate = LocalDate.now();
			}
			
			
			List<PaymentTransaction> transaction  = transactionRepo.filterAllTransactionByRefernceAndPaymentCard(
					referenceInclude.trim()
					,cardNumber.trim()
					,LocalDateTime.of(dateFrom, LocalTime.MIDNIGHT)
					,LocalDateTime.of(endDate, LocalTime.MAX));
			
			if(null !=  transaction && transaction.size() ==0) {
				 throw new NoSuchElementException("No data found for providied Filter Criteria ");
			}else {
				return transaction;
			}
		}catch (NoSuchElementException e) {
			ErrorResponse error = new ErrorResponse();
			error.setErrorMessage(e.getMessage());
			error.setTimestamp(LocalDateTime.now());
			throw new ZilchAppException(error, HttpStatus.BAD_REQUEST);
		}catch (Exception e) {
			e.printStackTrace();
			ErrorResponse error = new ErrorResponse();
			logger.error("Inside ManageTransactionService -> loadTransactions() Exception Happend", e);
			error.setErrorMessage(e.getMessage());
			error.setTimestamp(LocalDateTime.now());
			throw new ZilchAppException(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	    @Cacheable(cacheNames = "transactionStore", key = "#payment.idempotencykey") 
		public PaymentTransaction doPayment(String principal, PaymentTransaction payment) {
			
			Optional<PaymentTransaction>  transOpt = 	transactionRepo.findOneByKeyAndCard(payment.getIdempotencykey(), payment.getPaymentCard().getCardnumber());
			if(transOpt.isPresent()) {
				logger.debug("Transaction Already Exist !!!!  Duplicate/Retried Transaction  ");
				return transOpt.get();
			}
			
			List<PaymentCard> cardList = paymentCardRepo.findByCustomerCardnumber(principal,payment.getPaymentCard().getCardnumber());
			LocalDate todayDate  = LocalDate.now();
			
			
			Optional<PaymentCard> paymentCardOptional = cardList.stream()
					.filter(card -> card.getCvc().trim().equalsIgnoreCase(payment.getPaymentCard().getCvc().trim()))
					.filter(card -> card.getExpirationdate().isAfter(todayDate))
					.filter(card -> card.getCardbalance() >= payment.getTransactionamount()).findFirst();


			if (!paymentCardOptional.isPresent()) {
				ErrorResponse error = new ErrorResponse();
				error.setErrorMessage("Invalid Payment Card details or Insufficient Funds Available");
				error.setTimestamp(LocalDateTime.now());
				throw new ZilchAppException(error, HttpStatus.BAD_REQUEST);
			}
			
			
			paymentCardOptional.ifPresent(card -> {
				card.doDebit(payment.getTransactionamount());
				card.addTransactions(payment);
				payment.setTransactionstatus(true);
				paymentCardRepo.saveAndFlush(card);	
			});
			
			Optional<PaymentTransaction> transOptional = paymentCardRepo.getReferenceById(paymentCardOptional.get().getObjectID()).getTransactions().stream().
					 filter(trans -> trans.getIdempotencykey().equalsIgnoreCase(payment.getIdempotencykey())).findFirst();
			return transOptional.get();
			
		}
		
		
	
	

}
