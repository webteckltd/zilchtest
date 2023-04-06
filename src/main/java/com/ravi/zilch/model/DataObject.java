package com.ravi.zilch.model;


import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DataObject implements Serializable {
	
	@Id 
	@GeneratedValue
	@UuidGenerator
	@Column(name = "objectid")
	@JsonProperty(value = "referenceID")
	protected String objectID;
	
	@CreationTimestamp
	@Column(name = "createdate")
	@JsonIgnore
	protected LocalDateTime createDate ;
	
	@UpdateTimestamp
	@Column(name = "lastupdatedate")
	@JsonIgnore
	protected LocalDateTime lastUpdateDate;

	
	public String getObjectID() {
		return objectID;
	}

	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public LocalDateTime getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	
	
}
