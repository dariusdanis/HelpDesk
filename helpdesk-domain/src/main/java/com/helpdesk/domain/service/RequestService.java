package com.helpdesk.domain.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.helpdesk.domain.dao.RequestDao;
import com.helpdesk.domain.entity.RequestEntity;
import com.helpdesk.domain.entity.UserEntity;

@Service
public class RequestService {

	@Autowired
	private RequestDao requsetDao;

	@Transactional
	public RequestEntity merge(RequestEntity requestEntity) {
		return requsetDao.merge(requestEntity);
	}

	@Transactional
	public List<RequestEntity> getAll(int from) {
		return requsetDao.getAll(from);
	}

	@Transactional
	public List<RequestEntity> getAllByCreatOrBelongsTo(UserEntity userEntity) {
		return requsetDao.getAllByCreatOrBelongsTo(userEntity);
	}

	@Transactional
	public RequestEntity loadById(int id) {
		return requsetDao.loadById(id);
	}

	@Transactional
	public List<RequestEntity> getAllUnassigned() {
		return requsetDao.getAllUnassigned();
	}

	@Transactional
	public List<RequestEntity> getAllByEngineer(UserEntity engineerEntity) {
		return requsetDao.getAllByEngineer(engineerEntity);
	}

	@Transactional
	public List<RequestEntity> getAllByEngineerAndStatus(
			UserEntity engineerEntity, String status, int from) {
		return requsetDao.getAllByEngineerAndStatus(engineerEntity, status,
				from);
	}

	@Transactional
	public List<RequestEntity> getAllByStatus(String status, int from) {
		return requsetDao.getAllByStatus(status, from);
	}

	@Transactional
	public List<RequestEntity> getAllByEngineerAndNotStatus(
			UserEntity engineerEntity, String status, int from) {
		return requsetDao.getAllByEngineerAndNotStatus(engineerEntity, status,
				from);
	}

	@Transactional
	public List<Integer> getTopThree() {
		return requsetDao.getTopThree();
	}

	@Transactional
	public List<RequestEntity> getAllByAdminAndNotStatus(UserEntity admin,
			String status, int from) {
		return requsetDao.getAllByAdminAndNotStatus(admin, status, from);
	}

	@Transactional
	public List<RequestEntity> getAllByBelongsTosAndNotStatu(
			UserEntity belongsTo, String status, String status2, int from) {
		return requsetDao.getAllByBelongsTosAndNotStatu(belongsTo, status,
				from, status2);
	}

	@Transactional
	public List<RequestEntity> getAllByBelongsToAndStatus(UserEntity belongsTo,
			String status, String status2, int from) {
		return requsetDao.getAllByBelongsToAndStatus(belongsTo, status,
				status2, from);
	}

	@Transactional
	public List<RequestEntity> getAllByStatusOrAssignetToUser(String status,
			UserEntity user, int from) {
		return requsetDao.getAllByStatusOrAssignetToUser(status, user, from);
	}

	@Transactional
	public List<RequestEntity> getDirectorHistory(UserEntity director,
			String solved, String assigned, int from) {
		return requsetDao.getDirectorHistory(director, solved, assigned, from);
	}

	@Transactional
	public ArrayList<List<Object>> getAllOverDoRequest(Date startDate,
			Date endDate) {
		ArrayList<List<Object>> entities = new ArrayList<List<Object>>();
		List<RequestEntity> allEntities = requsetDao.getAllOverDoRequest(
				startDate, endDate);
		Calendar cal = Calendar.getInstance();
		for (RequestEntity entity : allEntities) {
			Date solveDate = entity.getSolveDate() == null ? new Date()
					: entity.getSolveDate();
			cal.setTime(solveDate);
			DateTime solveDateDT = new DateTime(cal.get(Calendar.YEAR),
					cal.get(Calendar.MONTH) + 1,
					cal.get(Calendar.DAY_OF_MONTH),
					cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));

			cal.setTime(entity.getRequestDate());
			DateTime createDateDT = new DateTime(cal.get(Calendar.YEAR),
					cal.get(Calendar.MONTH) + 1,
					cal.get(Calendar.DAY_OF_MONTH),
					cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));

			Minutes diff = Minutes.minutesBetween(createDateDT, solveDateDT);
			Integer diffInteger = Integer.valueOf(diff.toString().substring(2,
					diff.toString().length() - 1));
			List<Object> list = new ArrayList<Object>();
			if (entity.getTypeEntity().getType().equals("REQ")) {
				if ((diffInteger / 60) >= Integer.valueOf(entity
						.getFacilityEntity().getLhReq())) {
					list.add(entity);
					list.add(diffInteger);
					entities.add(list);
				}
			} else {
				if ((diffInteger / 60) >= Integer.valueOf(entity
						.getFacilityEntity().getLhInc())) {
					list.add(entity);
					list.add(diffInteger);
					entities.add(list);
				}
			}
		}
		return entities;
	}

	@Transactional
	public Long getAllByStatusCount(String status) {
		return requsetDao.getAllByStatusCount(status);
	}

	@Transactional
	public Long getAllByAdminAndNotStatusCount(UserEntity user, String status) {
		return requsetDao.getAllByAdminAndNotStatusCount(user, status);
	}

	@Transactional
	public Long getAllCount() {
		return requsetDao.getAllCount();
	}

	@Transactional
	public Long getAllByBelongsTosAndNotStatuCount(UserEntity belongsTo,
			String status, String status2) {
		return requsetDao.getAllByBelongsTosAndNotStatuCount(belongsTo, status,
				status2);
	}

	@Transactional
	public Long getAllByBelongsToAndStatusCount(UserEntity belongsTo,
			String status, String status2) {
		return requsetDao.getAllByBelongsToAndStatusCount(belongsTo, status,
				status2);
	}

	@Transactional
	public Long getAllByEngineerAndStatusCount(UserEntity engineerEntity,
			String status) {
		return requsetDao
				.getAllByEngineerAndStatusCount(engineerEntity, status);
	}

	@Transactional
	public Long getAllByEngineerAndNotStatusCount(UserEntity engineerEntity,
			String status) {
		return requsetDao.getAllByEngineerAndNotStatusCount(engineerEntity,
				status);
	}
	
	@Transactional
	public Long getAllByStatusOrAssignetToUserCount(String status,
			UserEntity user) {
		return requsetDao.getAllByStatusOrAssignetToUserCount(status, user);
	}

	@Transactional
	public Long getDirectorHistoryCount(UserEntity director, String solved,
			String assigned) {
		return requsetDao.getDirectorHistoryCount(director, solved, assigned);
	}

	@Transactional
	public Long getTodaySolvedCount(Date today) {
		return requsetDao.getTodaySolvedCount(today);
	}

	@Transactional
	public Long getTodayNewReq(Date today) {
		return requsetDao.getTodayNewReq(today);
	}

	@Transactional
	public Long getTodayNewInfoReq(Date today) {
		return requsetDao.getTodayNewInfoReq(today);
	}

}
