package com.helpdesk.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.helpdesk.domain.dao.RoleDao;
import com.helpdesk.domain.entity.RoleEntity;

@Service
public class RoleService {
	
	@Autowired
	private RoleDao roleDao;
	
	@Transactional
	public List<RoleEntity> getAll() {
		return roleDao.getAll();
	}
	
}
