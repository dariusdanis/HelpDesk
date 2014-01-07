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
	public UserEntity merge(UserEntity userEntity) {
		return userDao.merge(userEntity);
	}
	
	@Transactional
	public List<UserEntity> findAllByRole(String role) {
		return userDao.findAllByRole(role);
	}
	
	@Transactional
	public List<UserEntity> findAllByRole(String role, String secondRole) {
		List<UserEntity> entities = userDao.findAllByRole(role);
		entities.addAll(userDao.findAllByRole(secondRole));
		return entities;
	}

	@Transactional
	public List<UserEntity> findAll() {
		return userDao.findAll();
	}
	
	@Transactional
	public UserEntity findByCompanyFK(int companyFK) {
		return userDao.findByCompanyId(companyFK);
	}

	public UserEntity findById(int id) {
		return userDao.findById(id);
	}
	
}
