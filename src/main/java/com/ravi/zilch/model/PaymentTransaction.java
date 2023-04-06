package com.ravi.zilch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "transaction") 
@JsonInclude(Include.NON_NULL)
public class PaymentTransaction extends DataObject {
	
	private static final long serialVersionUID = -6964046779345334504L;
	
	@NotNull(message = "Transaction Amount: positive value is required")
	@Min(value=1, message="Transaction Amount: positive number, min 1 is required")
	private Long  transactionamount;
	
	@NotBlank(message = "Transaction Refernence is required ")
	private String  transactionrefernce;
	
	private String  transactiondescription;
	
	@NotBlank(message = "Idempotency key is required ")
	private String  idempotencykey;
	
	private boolean   transactionstatus;
	private String  transactionfailurereason;
	
	@ManyToOne
	@JoinColumn(name = "zilchcardobjectid")
	@NotNull(message = "Payment Card Details are required ")
	private PaymentCard paymentCard;
	
	public PaymentCard getPaymentCard() {
		return paymentCard;
	}

	public void setPaymentCard(PaymentCard paymentCard) {
		this.paymentCard = paymentCard;
	}

	public Long getTransactionamount() {
		return transactionamount;
	}



	public void setTransactionamount(long transactionamount) {
		this.transactionamount = transactionamount;
	}


	public String getTransactionrefernce() {
		return transactionrefernce;
	}



	public void setTransactionrefernce(String transactionrefernce) {
		this.transactionrefernce = transactionrefernce;
	}



	public String getTransactiondescription() {
		return transactiondescription;
	}



	public void setTransactiondescription(String transactiondescription) {
		this.transactiondescription = transactiondescription;
	}



	public String getIdempotencykey() {
		return idempotencykey;
	}



	public void setIdempotencykey(String idempotencykey) {
		this.idempotencykey = idempotencykey;
	}



	public boolean getTransactionstatus() {
		return transactionstatus;
	}



	public void setTransactionstatus(boolean transactionstatus) {
		this.transactionstatus = transactionstatus;
	}



	public String getTransactionfailurereason() {
		return transactionfailurereason;
	}



	public void setTransactionfailurereason(String transactionfailurereason) {
		this.transactionfailurereason = transactionfailurereason;
	}

	@Override
	public String toString() {
		return "PaymentTransaction [transactionamount=" + transactionamount + ", transactionrefernce="
				+ transactionrefernce + ", transactiondescription=" + transactiondescription + ", idempotencykey="
				+ idempotencykey + ", transactionstatus=" + transactionstatus + ", transactionfailurereason="
				+ transactionfailurereason +  "]";
	}

	
	
	

}
