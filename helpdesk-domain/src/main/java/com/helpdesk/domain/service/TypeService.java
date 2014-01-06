package com.helpdesk.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.helpdesk.domain.dao.TypeDao;
import com.helpdesk.domain.entity.TypeEntity;

@Service
public class TypeService {

	@Autowired
	private TypeDao typeDao;
	
	@Transactional
	public List<TypeEntity> getAll() {
		return typeDao.getAll();
	}
	
}
