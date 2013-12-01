package com.helpdesk.domain.dao;

import java.util.List;

import com.helpdesk.domain.entity.TypeEntity;

public interface TypeDao {

	List<TypeEntity> getAll();
	
}
