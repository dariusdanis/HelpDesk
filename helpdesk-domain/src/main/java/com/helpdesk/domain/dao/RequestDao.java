package com.helpdesk.domain.dao;

import java.util.Date;
import java.util.List;

import com.helpdesk.domain.entity.RequestEntity;
import com.helpdesk.domain.entity.UserEntity;

public interface RequestDao {
	
	RequestEntity merge (RequestEntity requestEntity);
	
	List<RequestEntity> getAll();
	
	List<RequestEntity> getAllByCreatOrBelongsTo(UserEntity userEntity);

	List<RequestEntity> getAllUnassigned();
	
	RequestEntity loadById(int id);

	List<RequestEntity> getAllByEngineer(UserEntity engineerEntity);

	List<RequestEntity> getAllByEngineerAndStatus(UserEntity engineerEntity, String status);

	List<RequestEntity> getAllByStatus(String status);

	List<RequestEntity> getAllByEngineerAndNotStatus(UserEntity engineerEntity, String status);
	
	List<Integer> getTopThree();
	
	List<RequestEntity> getAllByAdminAndNotStatus(UserEntity user, String status);

	List<RequestEntity> getAllByBelongsTosAndNotStatu(UserEntity belongsTo, String status);

	List<RequestEntity> getAllByBelongsToAndStatus(UserEntity belongsTo, String status);

	List<RequestEntity> getAllByStatusOrAssignetToUser(String status, UserEntity user);

	List<RequestEntity> getDirectorHistory(UserEntity director, String solved, String assigned);
	
	List<RequestEntity> getAllOverDoRequest(Date startDate, Date endDate);
	
}
