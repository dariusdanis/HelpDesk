package com.helpdesk.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.helpdesk.domain.dao.CompanyDao;
import com.helpdesk.domain.entity.CompanyEntity;

@Service
public class CompanyService {

	@Autowired
	private CompanyDao companyDao;
	
	@Transactional
	public List<CompanyEntity> getAll() {
		return companyDao.getAll();
	}
	
}
