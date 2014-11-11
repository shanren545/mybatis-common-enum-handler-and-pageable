package com.jingwei.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jingwei.db.mapper.CityMapper;

@Service
public class CityServiceImpl implements CityService{
	
	@Autowired
	private CityMapper cityMapper;
	
	public void test(){
		cityMapper.selectByPrimaryKey(0);
		
	}

}
