package com.helpdesk.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.helpdesk.domain.dao.RequsetDao;
import com.helpdesk.domain.entity.RequestEntity;

@Service
public class RequestService {

	@Autowired
	private RequsetDao requsetDao;
	
	@Transactional
	public RequestEntity merge(RequestEntity requestEntity) {
		return requsetDao.merge(requestEntity);
	}
	
}
