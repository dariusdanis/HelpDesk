package com.helpdesk.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.helpdesk.domain.dao.UserDao;
import com.helpdesk.domain.entity.UserEntity;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Transactional
	public UserEntity findByEmail(String email) {		
		return userDao.findByEmail(email);
	}
	
}
