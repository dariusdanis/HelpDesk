package com.helpdesk.domain.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	private String phone;
	private String password;
	private boolean active;
	
	
	@ManyToOne
	@JoinColumn(name = "companyFk")
	private CompanyEntity companyEntity;
	
    @ManyToOne
    @JoinColumn(name = "roleFk")
	private RoleEntity roleEntity;
	
    @OneToMany(targetEntity = RequestEntity.class, fetch=FetchType.LAZY, mappedBy = "creatorEntity")
    private List<RequestEntity> creatorEntity;
    
    @OneToMany(targetEntity = RequestEntity.class, fetch=FetchType.LAZY, mappedBy = "engineerEntity")
    private List<RequestEntity> engineerEntity;
    
    @OneToMany(targetEntity = RequestEntity.class, fetch=FetchType.LAZY, mappedBy = "administratorEntity")
    private List<RequestEntity> administratorEntity;
    
    @OneToMany(targetEntity = NotificationEntity.class, fetch=FetchType.LAZY, mappedBy = "userEntity")
    private List<NotificationEntity> notifications;
    
    @OneToMany(targetEntity = RequestEntity.class, fetch=FetchType.LAZY, mappedBy = "requestBelongsTo")
    private List<RequestEntity> belongsTo;
    
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

	public List<NotificationEntity> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<NotificationEntity> notifications) {
		this.notifications = notifications;
	}

	public List<RequestEntity> getCreatorEntity() {
		return creatorEntity;
	}

	public void setCreatorEntity(List<RequestEntity> creatorEntity) {
		this.creatorEntity = creatorEntity;
	}

	public List<RequestEntity> getEngineerEntity() {
		return engineerEntity;
	}

	public void setEngineerEntity(List<RequestEntity> engineerEntity) {
		this.engineerEntity = engineerEntity;
	}

	public List<RequestEntity> getAdministratorEntity() {
		return administratorEntity;
	}

	public void setAdministratorEntity(List<RequestEntity> administratorEntity) {
		this.administratorEntity = administratorEntity;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public CompanyEntity getCompanyEntity() {
		return companyEntity;
	}

	public void setCompanyEntity(CompanyEntity companyEntity) {
		this.companyEntity = companyEntity;
	}

	@Override
	public String toString() {
		return name + " " + surname;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof UserEntity) {
			if (((UserEntity)obj).getId() == this.id) {
				return true;
			}
		}
		return false;
	}
	
}
