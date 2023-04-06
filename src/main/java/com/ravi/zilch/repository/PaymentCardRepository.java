package com.ravi.zilch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ravi.zilch.model.PaymentCard;

@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCard, String>{
	//List<PaymentCard> findByCardnumber(String cardnumber);
	
	@Query(value = "SELECT card FROM PaymentCard card WHERE card.custmer.email = ?1 and card.cardnumber = ?2")
	List<PaymentCard> findByCustomerCardnumber(String email,String cardnumber);
}
