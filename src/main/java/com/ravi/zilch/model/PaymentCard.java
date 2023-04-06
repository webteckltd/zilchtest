package com.ravi.zilch.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "zilchcard")  
@JsonInclude(Include.NON_NULL)
public class PaymentCard extends DataObject {
	private static final long serialVersionUID = -2918429475725217824L;
	
	@NotBlank(message = "16 Digit Card Number is required ")
	@Size(min=16, max=16) 
	private String cardnumber; 
	
	
	@DateTimeFormat(pattern="dd/mm/yyyy")
    @NotNull @FutureOrPresent
	private LocalDate expirationdate;
	
	@NotBlank(message = "3 Digit CVC is required  ")
	@Size(min=3, max=3) 
	private String cvc; 
	
	
	
	private long cardbalance;  
	
	@ManyToOne
	@JoinColumn(name = "custmerobjectid")
	private Customer custmer;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentCard",orphanRemoval = true)
	@JsonIgnore
	private List<PaymentTransaction> transactions = new ArrayList<PaymentTransaction>();
	

	public List<PaymentTransaction> getTransactions() {
		return transactions;
	}
	public void addTransactions(PaymentTransaction transactions) {
		this.transactions.add(transactions);
		transactions.setPaymentCard(this);
	}
	
	public void removeTransactions(PaymentTransaction transactions) {
		this.transactions.remove(transactions);
		transactions.setPaymentCard(null);
	}
	public String getCardnumber() {
		return cardnumber;
	}
	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}
	public LocalDate getExpirationdate() {
		return expirationdate;
	}
	public void setExpirationdate(LocalDate expirationdate) {
		this.expirationdate = expirationdate;
	}
	public String getCvc() {
		return cvc;
	}
	public void setCvc(String cvc) {
		this.cvc = cvc;
	}
	public long getCardbalance() {
		return cardbalance;
	}
	public void setCardbalance(long cardbalance) {
		this.cardbalance = cardbalance;
	}
	public Customer getCustmer() {
		return custmer;
	}
	public void setCustmer(Customer custmer) {
		this.custmer = custmer;
	}
	
	
	public void doDebit(long amount) {
		if(this.cardbalance >= amount)
		     this.cardbalance = this.cardbalance -amount ;
	}
	
	public void doCredit(long amount) {
		if(this.cardbalance >= amount)
		     this.cardbalance = this.cardbalance + amount ;
	}
	
	@Override
	public String toString() {
		return "PaymentCard [cardnumber=" + cardnumber + ", expirationdate=" + expirationdate + ", cvc=" + cvc
				+ ", cardbalance=" + cardbalance + ", custmer=" + custmer + "]";
	}
	
	

}
