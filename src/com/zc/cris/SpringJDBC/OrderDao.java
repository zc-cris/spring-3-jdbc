package com.zc.cris.SpringJDBC;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Order get(Integer id) {
		String sql = "select id,name,customer_id from orders where id = ?";
		RowMapper<Order> rowMapper = new BeanPropertyRowMapper<>(Order.class);
		Order order = this.jdbcTemplate.queryForObject(sql, rowMapper,id);
		return order;
	}
	
	
}
