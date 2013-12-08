package com.helpdesk.domain.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class NotificationEntity {

	@Id
	@GeneratedValue
	private int id;
	
    @ManyToOne
    @JoinColumn(name = "userFk")
	private UserEntity userEntity;
	
    @ManyToOne
    @JoinColumn(name = "requestFk")
	private RequestEntity requestEntity;
	
    @OneToOne(mappedBy="notificationEntity", cascade=CascadeType.ALL)
    private NotificationInfoEntity infoEntity;
    
	public NotificationEntity() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public RequestEntity getRequestEntity() {
		return requestEntity;
	}

	public void setRequestEntity(RequestEntity requestEntity) {
		this.requestEntity = requestEntity;
	}

	public NotificationInfoEntity getInfoEntity() {
		return infoEntity;
	}

	public void setInfoEntity(NotificationInfoEntity infoEntity) {
		this.infoEntity = infoEntity;
	}
	
}
