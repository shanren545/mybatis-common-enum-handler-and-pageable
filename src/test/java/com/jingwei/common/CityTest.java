package com.jingwei.common;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jingwei.PageInfo;
import com.jingwei.db.domain.City;
import com.jingwei.db.domain.CityExample;
import com.jingwei.db.domain.MyEnum;
import com.jingwei.db.mapper.CityMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/root-context.xml" })
public class CityTest {

	@Autowired
	private CityMapper cityMapper;

	@Test
	public void test() {
		City city = cityMapper.selectByPrimaryKey(1);
		Assert.assertNotNull(city);
		
		CityExample ex = new CityExample();
		ex.setPageInfo(new PageInfo(3,20,"name desc"));
		List<City> list = cityMapper.selectByExample(ex);
		Assert.assertEquals(20, list.size());
		list = cityMapper.selectByExampleWithBLOBs(ex);
		Assert.assertEquals(20, list.size());
		ex.setPageInfo(null);
		list = cityMapper.selectByExampleWithBLOBs(ex);
		Assert.assertTrue(20 < list.size());
		
		CityExample.Criteria c = ex.createCriteria();
		c.andCodeIn(Arrays.asList(MyEnum.A,MyEnum.B));
		list = cityMapper.selectByExampleWithBLOBs(ex);
		Assert.assertTrue(20 > list.size());

	}

}
