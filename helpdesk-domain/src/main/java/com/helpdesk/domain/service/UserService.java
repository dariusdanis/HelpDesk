package com.helpdesk.domain.service;

import java.util.List;

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
	
	@Transactional
	public void merge(UserEntity userEntity) {
		userDao.merge(userEntity);
	}
	
	@Transactional
	public List<UserEntity> findAllByRole(String role) {
		return userDao.findAllByRole(role);
	}
	
	
}
