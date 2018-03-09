package com.zc.cris.SpringTransaction;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Propagation;

import com.zc.cris.SpringTransaction.dao.BookStoreDao;
import com.zc.cris.SpringTransaction.service.BookStoreService;
import com.zc.cris.SpringTransaction.service.UserService;

class TestSpringTX {
	
	private ApplicationContext context = null;
	private BookStoreDao bookStoreDao= null;
	private BookStoreService bookStoreService = null;
	private UserService userService = null;
	
	{
		this.context = new ClassPathXmlApplicationContext("applicationContext.xml");
		this.bookStoreDao = context.getBean(BookStoreDao.class);
		this.bookStoreService = context.getBean(BookStoreService.class);
		this.userService = context.getBean(UserService.class);
	}
	
	/*
	 * 测试事务的传播行为
	 *	特别注意：测试的是两个不同接口的实现类，一个实现类的事务方法调用另外一个实现类的事务方法，才能
	 *	完美体现  propagation=Propagation.REQUIRES_NEW 的作用，如果在同一个接口的实现类测试将不会生效
	 */
	@Test
	void testTXchain() {
		
		this.userService.buyList("aa", Arrays.asList("123","125"));
//		this.bookStoreService.buyList("aa", Arrays.asList("125","123"));
		
	}
	
	
	
	
	/*
	 * 测试声明式事务
	 */
	@Test
	void testBuy() {
		this.bookStoreService.buy("aa", "123");
		
	}
	
	

	/*
	 * 测试更新用户的余额
	 */
	@Test
	void testUpdateUserBalance() {
		this.bookStoreDao.updateUserBalance("aa", 1000);
		
	}
	
	
	/*
	 * 测试更新书的库存
	 */
	@Test
	void testUpdateBookStock() {
		this.bookStoreDao.updateStoreStock("123");
		
	}
	
	/*
	 * 测试根据isbn号查询到图书的价格
	 */
	@Test
	void testGetPriceGyIsbn() {
		int price = this.bookStoreDao.getPriceByIsbn("123");
		System.out.println(price);
	}
	
	
	
	
}
