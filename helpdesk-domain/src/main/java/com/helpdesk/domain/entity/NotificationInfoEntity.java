package com.helpdesk.domain.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class NotificationInfoEntity {

	@Id
	@GeneratedValue
	private int id;

	@Column(nullable=false)
	private String notificationText;
	
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
	private Date date;
    
    @OneToOne
    @PrimaryKeyJoinColumn
    private NotificationEntity notificationEntity;
	
    public NotificationInfoEntity() {
	}
    
	public NotificationInfoEntity(String notificationText, Date date) {
		this.notificationText = notificationText;
		this.date = date;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNotificationText() {
		return notificationText;
	}

	public void setNotificationText(String notificationText) {
		this.notificationText = notificationText;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public NotificationEntity getNotificationEntity() {
		return notificationEntity;
	}

	public void setNotificationEntity(NotificationEntity notificationEntity) {
		this.notificationEntity = notificationEntity;
	}
	
}
