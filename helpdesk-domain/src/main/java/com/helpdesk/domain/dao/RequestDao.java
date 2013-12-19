package com.helpdesk.domain.dao;

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

	List<RequestEntity> getAllUnassignedAndStatus(String status);

	List<RequestEntity> getAllByEngineerAndNotStatus(UserEntity engineerEntity, String status);
	
}
