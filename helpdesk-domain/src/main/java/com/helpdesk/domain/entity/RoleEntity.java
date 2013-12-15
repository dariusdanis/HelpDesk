package com.helpdesk.domain.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class RoleEntity {
	
	@Id
	@GeneratedValue
	private Integer id;
	private String role;
	
	@OneToMany(targetEntity = UserEntity.class, fetch=FetchType.LAZY, mappedBy = "roleEntity")
	private List<UserEntity> userEntity;

	public RoleEntity() {
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RoleEntity(String role) {
		this.role = role;
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
