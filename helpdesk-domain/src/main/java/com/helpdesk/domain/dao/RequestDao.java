package com.helpdesk.domain.dao;

import java.util.Date;
import java.util.List;

import com.helpdesk.domain.entity.RequestEntity;
import com.helpdesk.domain.entity.UserEntity;

public interface RequestDao {
	
	RequestEntity merge (RequestEntity requestEntity);
	
	List<RequestEntity> getAll(int from);
	
	List<RequestEntity> getAllByCreatOrBelongsTo(UserEntity userEntity);

	List<RequestEntity> getAllUnassigned();
	
	RequestEntity loadById(int id);

	List<RequestEntity> getAllByEngineer(UserEntity engineerEntity);

	List<RequestEntity> getAllByEngineerAndStatus(UserEntity engineerEntity, String status, int from);

	List<RequestEntity> getAllByStatus(String status, int from);

	List<RequestEntity> getAllByEngineerAndNotStatus(UserEntity engineerEntity, String status, int from);
	
	List<Integer> getTopThree();
	
	List<RequestEntity> getAllByAdminAndNotStatus(UserEntity user, String status, int from);

	List<RequestEntity> getAllByBelongsTosAndNotStatu(UserEntity belongsTo, String status, int from, String status2);

	List<RequestEntity> getAllByBelongsToAndStatus(UserEntity belongsTo, String status, String status2, int from);

	List<RequestEntity> getAllByStatusOrAssignetToUser(String status, UserEntity user, int from);

	List<RequestEntity> getDirectorHistory(UserEntity director, String solved, String assigned, int from);
	
	List<RequestEntity> getAllOverDoRequest(Date startDate, Date endDate);
	
	Long getAllByStatusCount(String status);
	
	Long getAllByAdminAndNotStatusCount(UserEntity user, String status);
	
	Long getAllCount();
	
	Long getAllByBelongsTosAndNotStatuCount(UserEntity belongsTo, String status, String status2);
	
	Long getAllByBelongsToAndStatusCount(UserEntity belongsTo, String status, String status2);

	Long getAllByEngineerAndNotStatusCount(UserEntity engineerEntity,
			String status);

	Long getAllByEngineerAndStatusCount(UserEntity engineerEntity, String status);

	Long getAllByStatusOrAssignetToUserCount(String status, UserEntity user);

	Long getDirectorHistoryCount(UserEntity director, String solved, String assigned);
	
}
