package com.helpdesk.domain.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class RequestEntity {

	@Id
	@GeneratedValue
	private int id;
	
	@Lob
	@Column(nullable = false, columnDefinition="TEXT", length=9000)
	private String requestText;
	
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;

    @Column(nullable = false)
    private String summary;
    
    @ManyToOne
    @JoinColumn(name = "typeFk")
    private TypeEntity typeEntity;
    
    @ManyToOne
    @JoinColumn(name = "userFk")
    private UserEntity userEntity;
    
	@OneToMany(targetEntity = NotificationEntity.class, mappedBy = "requestEntity", fetch = FetchType.LAZY)
	private List<NotificationEntity> notificationEntities;
    
    public RequestEntity() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRequestText() {
		return requestText;
	}

	public void setRequestText(String requestText) {
		this.requestText = requestText;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public TypeEntity getTypeEntity() {
		return typeEntity;
	}

	public void setTypeEntity(TypeEntity typeEntity) {
		this.typeEntity = typeEntity;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<NotificationEntity> getNotificationEntities() {
		return notificationEntities;
	}

	public void setNotificationEntities(
			List<NotificationEntity> notificationEntities) {
		this.notificationEntities = notificationEntities;
	}

}
