package com.helpdesk.domain.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TypeEntity {

	@Id
	@GeneratedValue
	private int id;
	
	@Column(nullable = false)
	private String type;

	@OneToMany(targetEntity = RequestEntity.class, fetch=FetchType.LAZY, mappedBy = "typeEntity")
	private List<RequestEntity> requestEntity;
	
	public TypeEntity() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<RequestEntity> getRequestEntity() {
		return requestEntity;
	}

	public void setRequestEntity(List<RequestEntity> requestEntity) {
		this.requestEntity = requestEntity;
	}
	
	@Override
	public String toString() {
		return type;
	}

}
