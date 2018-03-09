package com.zc.cris.SpringJDBC;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

//不推荐使用 Spring 提供的 jdbcDaoSupport 类，而是直接使用 jdbcTemplate 作为dao类的成员变量
@Repository
public class CustomerDao extends JdbcDaoSupport{
	
	//如果继承 JdbcDaoSupport 就必须注入dataSource，
	//通过自定义setter方法将dataSource注入 JdbcDaoSupport的final类型方法setDataSource
	//麻烦，而是占用了宝贵的继承关系
	@Autowired
	public void setDataSource2(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	public Customer get(Integer id) {
		String sql = "select id,name from customers where id = ?";
		RowMapper<Customer> rowMapper = new BeanPropertyRowMapper<>(Customer.class);
		return getJdbcTemplate().queryForObject(sql, rowMapper, 1);
	}
}
