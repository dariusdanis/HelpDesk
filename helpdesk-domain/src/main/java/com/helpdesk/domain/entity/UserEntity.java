package com.helpdesk.domain.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class UserEntity {

	@Id
	@GeneratedValue
	private int id;
	private String name;
	private String surname;
	private String email;
	private Long phone;
	private String password;
	
    @ManyToOne
    @JoinColumn(name = "roleFk")
	private RoleEntity roleEntity;
	
    @OneToMany(targetEntity = RequestEntity.class, cascade = CascadeType.ALL, mappedBy = "userEntity")
    private List<RequestEntity> requestEntity;
    
	public UserEntity() {}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public RoleEntity getRoleEntity() {
		return roleEntity;
	}

	public void setRoleEntity(RoleEntity roleEntity) {
		this.roleEntity = roleEntity;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public List<RequestEntity> getRequestEntity() {
		return requestEntity;
	}

	public void setRequestEntity(List<RequestEntity> requestEntity) {
		this.requestEntity = requestEntity;
	}

}
