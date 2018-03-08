package com.zc.cris.SpringJDBC;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

class TestSpringJDBC {

	ApplicationContext context = null;
	JdbcTemplate jdbcTemplate = null;
	{
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");
	}
	
	
	@Test
	void testQueryForList() {
		
		
		
	}
	
	
	/*
	 * 从数据库获得一条记录，实际得到一个对象
	 * 1. RowMapper 指定如何去映射结果集的行，常用的实现类就是 BeanPropertyRowMapper
	 * 2. 可以使用 sql 语句的别名完成列名和属性名的映射
	 * 3. 不支持级联操作，所以jdbcTmplate 到底还是简单的封装了jdbc，毕竟不是一个ORM框架，这也是为什么Spring
	 * 		需要整合第三方的ORM成熟框架（把自己不擅长的交给擅长的人去做）
	 */
	@Test
	void testQueryForOne() {
		 String sql = "select ID,NAME,CUSTOMER_ID as 'customer.id' from orders where id = ?";
		 RowMapper<Order> rowMapper = new BeanPropertyRowMapper<>(Order.class);
		 Order order = this.jdbcTemplate.queryForObject(sql, rowMapper,1);
		 System.out.println(order);
		
	}
	
	/*
	 * 测试批量insert，delete，update
	 * 为什么批量删除的参数是Object[]类型的List集合类型？自己想想
	 */
	@Test
	void testBatchUpdate() {
		String sql = "update customers set NAME = ? where id=?";
		
		List<Object[]> batchArgs = new ArrayList<>();
		batchArgs.add(new Object[] {"AA",1});
		batchArgs.add(new Object[] {"BB",2});
		batchArgs.add(new Object[] {"CC",3});
		this.jdbcTemplate.batchUpdate(sql, batchArgs);
		
	}
	
	
	/*
	 * 测试update，delete，insert
	 */
	@Test
	void testUpdate() {
		String sql = "insert into customers (NAME) values (?)";
		this.jdbcTemplate.update(sql, "重庆吴亦凡");
		
	}
	
	
	/*
	 * 先测试一波能否连上数据库，完美！
	 */
	@Test
	void test() throws SQLException {
		
		Connection connection = jdbcTemplate.getDataSource().getConnection();
		System.out.println(connection);
		
	}
}
