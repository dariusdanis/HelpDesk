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
	
	@Column(nullable=false)
	private String status;
	
	@Lob
	@Column(nullable = false, columnDefinition="TEXT", length=9000)
	private String requestText;
	
	@Lob
	@Column(columnDefinition="TEXT", length=9000)
	private String requestSolution;
	
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;

    @Column(nullable = false)
    private String summary;
    
    @ManyToOne
    @JoinColumn(name = "typeFk")
    private TypeEntity typeEntity;
    
    @ManyToOne
    @JoinColumn(name = "belongsToFk")
    private UserEntity requestBelongsTo;
    
    @ManyToOne
    @JoinColumn(name = "creatorFk")
    private UserEntity creatorEntity;
    
    @ManyToOne
    @JoinColumn(name = "engineerFk")
    private UserEntity engineerEntity;
    
    @ManyToOne
    @JoinColumn(name = "administratorFk")
    private UserEntity administratorEntity;
    
	@OneToMany(targetEntity = NotificationEntity.class, mappedBy = "requestEntity", fetch = FetchType.LAZY)
	private List<NotificationEntity> notificationEntities;
	
    @ManyToOne
    @JoinColumn(name = "facilityFk")
	private FacilityEntity facilityEntity;
	
    @Temporal(TemporalType.TIMESTAMP)
    private Date solveDate;
    
	@Lob
	@Column(columnDefinition="TEXT", length=9000)
    private String whatWasDone;
    
	private String receiptMethod;
	private String timeSpend;
	private Integer parentRequsetId;
	
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UserEntity getCreatorEntity() {
		return creatorEntity;
	}

	public void setCreatorEntity(UserEntity creatorEntity) {
		this.creatorEntity = creatorEntity;
	}

	public UserEntity getEngineerEntity() {
		return engineerEntity;
	}

	public void setEngineerEntity(UserEntity engineerEntity) {
		this.engineerEntity = engineerEntity;
	}

	public UserEntity getAdministratorEntity() {
		return administratorEntity;
	}

	public void setAdministratorEntity(UserEntity administratorEntity) {
		this.administratorEntity = administratorEntity;
	}

	public Integer getParentRequsetId() {
		return parentRequsetId == null ? new Integer(0) : parentRequsetId;
	}

	public void setParentRequsetId(RequestEntity requestEntity) {
		this.parentRequsetId = new Integer(requestEntity.getId()); 
	}	
	
	public String getRequestSolution() {
		return requestSolution;
	}

	public void setRequestSolution(String requestSolution) {
		this.requestSolution = requestSolution;
	}
	
	public FacilityEntity getFacilityEntity() {
		return facilityEntity;
	}

	public void setFacilityEntity(FacilityEntity facilityEntity) {
		this.facilityEntity = facilityEntity;
	}

	public String getWhatWasDone() {
		return whatWasDone;
	}

	public void setWhatWasDone(String whatWasDone) {
		this.whatWasDone = whatWasDone;
	}

	public String getTimeSpend() {
		return timeSpend;
	}

	public void setTimeSpend(String timeSpend) {
		this.timeSpend = timeSpend;
	}
	
	public Date getSolveDate() {
		return solveDate;
	}

	public void setSolveDate(Date solveDate) {
		this.solveDate = solveDate;
	}
	
	public String getReceiptMethod() {
		return receiptMethod;
	}

	public void setReceiptMethod(String receiptMethod) {
		this.receiptMethod = receiptMethod;
	}
	
	public UserEntity getRequestBelongsTo() {
		return requestBelongsTo;
	}

	public void setRequestBelongsTo(UserEntity requestBelongsTo) {
		this.requestBelongsTo = requestBelongsTo;
	}

	@Override
	public String toString() {
		return String.valueOf(id);
	}
	
}
