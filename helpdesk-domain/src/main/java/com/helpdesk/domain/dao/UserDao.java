package com.helpdesk.domain.dao;

import com.helpdesk.domain.entity.UserEntity;

public interface UserDao {

	UserEntity findByEmail(String email);
	void merge (UserEntity userEntity);
}
