package com.helpdesk.domain.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class RoleEntity {
	
	@Id
	@GeneratedValue
	private int id;
	private String role;
	
	@OneToMany(targetEntity = UserEntity.class, cascade = CascadeType.ALL, mappedBy = "roleEntity")
	private List<UserEntity> userEntity;

	public RoleEntity() {
	}
	
	public RoleEntity(String role) {
		this.role = role;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<UserEntity> getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(List<UserEntity> userEntity) {
		this.userEntity = userEntity;
	}
	
	@Override
	public String toString() {
		return role.toLowerCase();
	}
	
}
