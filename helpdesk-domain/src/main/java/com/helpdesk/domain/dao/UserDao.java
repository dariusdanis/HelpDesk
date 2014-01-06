package com.helpdesk.domain.dao;

import java.util.List;

import com.helpdesk.domain.entity.UserEntity;

public interface UserDao {

	UserEntity findByEmail(String email);
	UserEntity merge (UserEntity userEntity);
	List<UserEntity> findAllByRole(String role);
	List<UserEntity> findAll();
	UserEntity findById(int id);
	
}
