package com.zc.cris.SpringJDBC;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

class TestSpringJDBC {

	ApplicationContext context = null;
	JdbcTemplate jdbcTemplate = null;
	{
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");
	}
	
	/*
	 * 使用聚名参数模板的时候，我们其实还有比下面更为简洁的方法，那就是可以使用update（String sql，SqlParameterSource parameterSource）方法
	 * 1. sql 参数的名字必须和类的属性名一致
	 * 2. 使用 SqlParameterSource 的实现类 BeanPropertySqlParameterSource 作为参数
	 */
	@Test
	void testNamedParameterJdbcTemplate2() {
		String sql = "insert into orders (name,customer_id) values (:name,:customerId)";
		Order order = new Order();
		order.setCustomerId(1);
		order.setName("洗面奶");
		
		SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(order);
		
		NamedParameterJdbcTemplate bean = this.context.getBean(NamedParameterJdbcTemplate.class);
		bean.update(sql, parameterSource);
	}
	
	/*
	 * 聚名参数 jdbcTemplate 模板可以为参数取易懂的名字，比？占位符直观的多
	 * 1. 好处：若有多个参数，不用对位值，直接对应参数名字，便于维护
	 * 2. 缺点：比较麻烦
	 */
	@Test
	void testNamedParameterJdbcTemplate() {
		String sql = "insert into orders (name,customer_id) values (:name,:customer_id)";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("name", "内裤");
		paramMap.put("customer_id", 2);
		NamedParameterJdbcTemplate bean = (NamedParameterJdbcTemplate) this.context.getBean("namedJdbcTemplate");
		bean.update(sql, paramMap);
	}
	
	
	/*
	 * 测试继承Spring提供的 jdbcDaoSupport 类
	 */
	@Test
	void testJdbcDaoSupport() {
//		CustomerDao bean = (CustomerDao) this.context.getBean("customerDao");
		CustomerDao customerDao = this.context.getBean(CustomerDao.class);
		System.out.println(customerDao.get(1));
	}
	
	
	/*
	 * 如何在dao层使用jdbcTemplate
	 */
	@Test
	void testDaoJdbcTemplate() {
		OrderDao orderDao = (OrderDao) this.context.getBean("orderDao");
		System.out.println(orderDao.get(1));
	}
	
	
	
	/*
	 * 获取单列的值或者统计查询
	 */
	@Test
	void testQueryForObject() {
		//统计函数
		String sql = "select count(id) from orders";
		Long count = this.jdbcTemplate.queryForObject(sql, Long.class);
		System.out.println(count);
		
		//查询特定列的值
		sql = "select name from orders";
		RowMapper<String> rowMapper = new SingleColumnRowMapper<>();
		List<String> names = this.jdbcTemplate.query(sql,rowMapper);
		System.out.println(names);
	}
	
	
	
	
	/*
	 * 从数据库根据查询条件查询多条记录
	 */
	@Test
	void testQueryForList() {
		String sql = "select ID,NAME,CUSTOMER_ID from orders where id > ?";
		RowMapper<Order> rowMapper = new BeanPropertyRowMapper<>(Order.class);
		List<Order> orders = this.jdbcTemplate.query(sql, rowMapper,2);
		System.out.println(orders); 
		
	}
	
	
	/*
	 * 从数据库获得一条记录，实际得到一个对象
	 * 1. RowMapper 指定如何去映射结果集的行，常用的实现类就是 BeanPropertyRowMapper
	 * 2. 可以使用 sql 语句的别名完成列名和属性名的映射
	 * 3. 不支持级联操作，所以jdbcTmplate 到底还是简单的封装了jdbc，毕竟不是一个ORM框架，这也是为什么Spring
	 * 		需要整合第三方的ORM成熟框架（把自己不擅长的交给擅长的去做）
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
