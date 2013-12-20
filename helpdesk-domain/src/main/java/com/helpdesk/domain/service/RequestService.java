package com.helpdesk.domain.service;

import java.util.List;

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
	public List<RequestEntity> getAll() {
		return requsetDao.getAll();
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
			UserEntity engineerEntity, String status) {
		return requsetDao.getAllByEngineerAndStatus(engineerEntity, status);
	}

	@Transactional
	public List<RequestEntity> getAllUnassignedAndStatus(String status) {
		return requsetDao.getAllUnassignedAndStatus(status);
	}

	@Transactional
	public List<RequestEntity> getAllByEngineerAndNotStatus(UserEntity engineerEntity, String status) {
		return requsetDao.getAllByEngineerAndNotStatus(engineerEntity, status);
	}
	
	@Transactional
	public List<Integer> getTopThree() {
		return requsetDao.getTopThree();
	}
	
}
