package com.zc.cris.SpringTransaction.dao;

import static org.hamcrest.CoreMatchers.is;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.zc.cris.SpringTransaction.exception.AccountBalanceException;
import com.zc.cris.SpringTransaction.exception.BookStockException;

@Repository(value="bookStoreDao")
public class BookStoreDaoImpl implements BookStoreDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Override
	public int getPriceByIsbn(String isbn) {
		String sql = "select price from book where isbn = ?";
		return this.jdbcTemplate.queryForObject(sql, Integer.class, isbn);
	}

	@Override
	public void updateStoreStock(String isbn) {
		//因为mysql 不支持check约束
		String sql = "select stock from book_stock where isbn = ?";
		Integer count = this.jdbcTemplate.queryForObject(sql, Integer.class, isbn);
		if(count == 0) {
			throw new BookStockException("库存不足");
		}
		
		sql = "update book_stock set stock = stock -1 where isbn = ?";
		this.jdbcTemplate.update(sql, isbn);
	}

	@Override
	public void updateUserBalance(String userName, int price) {
		
		String sql = "select balance from account where username=?";
		Integer balance = this.jdbcTemplate.queryForObject(sql, Integer.class, userName);
		if(balance < price) {
			throw new AccountBalanceException("用户余额不足");
		}
		
		sql = "update account set balance = balance - ? where username = ?";
		this.jdbcTemplate.update(sql, price, userName);
	}
	
	
	
	
}
