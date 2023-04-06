package com.ravi.zilch.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "customer")  
@JsonInclude(Include.NON_NULL)
public class Customer extends DataObject {
	
	private static final long serialVersionUID = 1716175155517291488L;
	
	@NotBlank(message = "First Name must be provided")
	private String firstname;
	
	@NotBlank(message = "Last Name must be provided")
	private String lastname ;
	
	@NotEmpty @Email(message = "Provide Valid email")
	private String email;
	
	private String contactnumber;
	
	@NotBlank(message = "Password must be set")
	@JsonIgnore
	private String password;
	
	@NotBlank(message = "Customer Role must be set")
	@JsonIgnore
	private String customerrole;
	
	
	@OneToMany(fetch = FetchType.EAGER , cascade = CascadeType.ALL, mappedBy = "custmer",orphanRemoval = true)
	@JsonIgnore
	private List<PaymentCard> cards = new ArrayList<PaymentCard>();
	
	public List<PaymentCard> getCards() {
		return cards;
	}
	
	public void addCard(PaymentCard card) {
		this.cards.add(card);
		card.setCustmer(this);
	}
	
	public void removeCard(PaymentCard card) {
		this.cards.remove(card);
		card.setCustmer(null);
	}
	
	
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getContactnumber() {
		return contactnumber;
	}

	public void setContactnumber(String contactnumber) {
		this.contactnumber = contactnumber;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCustomerrole() {
		return customerrole;
	}

	public void setCustomerrole(String customerrole) {
		this.customerrole = customerrole;
	}
	
	
}
