package com.helpdesk.domain.dao;

import java.util.List;

import com.helpdesk.domain.entity.RoleEntity;

public interface RoleDao {

	List<RoleEntity> getAll();
	
}
