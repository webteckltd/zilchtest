package com.ravi.zilch.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ravi.zilch.model.PaymentTransaction;

public interface TransactionRepository extends JpaRepository<PaymentTransaction, String> {

	@Query(value = "SELECT t FROM PaymentTransaction t WHERE t.objectID =?2 and t.paymentCard.custmer.email = ?1")
	Optional<PaymentTransaction> findOneTransactionForUser(String email, String id);

	@Query(value = "SELECT t FROM PaymentTransaction t WHERE  t.paymentCard.custmer.email = ?1")
	List<PaymentTransaction> findAllWithCustomer(String email);

	@Query(value = "SELECT t FROM PaymentTransaction t "
			+ "where UPPER(t.transactionrefernce) like CONCAT('%',UPPER(?1),'%')  "
			+ "and  t.paymentCard.cardnumber like CONCAT('%',?2,'%') " + "and t.createDate >=  ?3 "
			+ "and t.createDate <=  ?4 ")
	List<PaymentTransaction> filterAllTransactionByRefernceAndPaymentCard(String refernce, String cardNumber,
			LocalDateTime start, LocalDateTime end);
	
	@Query(value = "SELECT t FROM PaymentTransaction t WHERE t.idempotencykey =?1 and t.paymentCard.cardnumber = ?2")
	Optional<PaymentTransaction>  findOneByKeyAndCard(String key, String cardNumber);

}
