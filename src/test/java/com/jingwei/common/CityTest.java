package com.jingwei.common;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jingwei.PageInfo;
import com.jingwei.config.AppConfig;
import com.jingwei.db.domain.City;
import com.jingwei.db.domain.CityExample;
import com.jingwei.db.domain.MyEnum;
import com.jingwei.db.mapper.CityMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
// @ContextConfiguration({ "/root-context.xml" })
public class CityTest {

	@Autowired
	private CityMapper cityMapper;

	@Resource(name = "datasource")
	private DataSource ds;

	@Autowired
	private List<DataSource> list;

	@Autowired
	private ApplicationContext ctx;

	@Test
	public void test() {
		City city = cityMapper.selectByPrimaryKey(1);
		Assert.assertNotNull(city);

		Map<String, DataSource> map = ctx.getBeansOfType(DataSource.class);

		CityExample ex = new CityExample();
		ex.setPageInfo(new PageInfo(3, 20, "name desc"));
		List<City> list = cityMapper.selectByExample(ex);
		Assert.assertEquals(20, list.size());
		list = cityMapper.selectByExampleWithBLOBs(ex);
		Assert.assertEquals(20, list.size());
		ex.setPageInfo(null);
		list = cityMapper.selectByExampleWithBLOBs(ex);
		Assert.assertTrue(20 < list.size());

		CityExample.Criteria c = ex.createCriteria();
		c.andCodeIn(Arrays.asList(MyEnum.A, MyEnum.B));
		list = cityMapper.selectByExampleWithBLOBs(ex);
		Assert.assertTrue(20 > list.size());

	}

}
