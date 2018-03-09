package com.zc.cris.SpringTransaction.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zc.cris.SpringTransaction.dao.BookStoreDao;
import com.zc.cris.SpringTransaction.exception.AccountBalanceException;

@Service(value="bookStoreService")
public class BookStoreServiceImpl implements BookStoreService {

	@Autowired
	private BookStoreDao bookStoreDao;
	
	@Transactional
	@Override
	public void buyList(String username, List<String> isbns) {
		for(String isbn : isbns) {
			buy(username, isbn);
		}
	}
	
	//添加事务注解
	//1. 使用propagation 指定事物的传播行为，即当前的事务访问被另外一个事务方法调用的时候，
	//如何使用事务，默认取值为 REQUIRED,即使用调用方法的事务
	//取值为 REQUIRES_NEW : 使用自己的事务，调用方法的事务被挂起
	//2. 使用 isolation 可以指定事务的隔离级别，最常用的取值为 READ_COMMITTED 
	//3. 默认情况下，Spring 的声明式事务对所有的异常进行回滚，可以通过响应的属性设置哪种
	//异常不进行回滚，通常情况下，默认即可
	//4. 使用 readOnly 指定事务是否为 只读，表示这个事务 只读取数据库的信息，而不做任何修改，
	//这样子可以帮助数据库引擎优化事务的执行效率,需要设置为true
	//5. 使用 timeout 可以指定强制事务回滚之前的最长占用时间，举个例子：
	// 设定2秒后事务还未执行完，就强制性进行事务回滚
	@Transactional(propagation=Propagation.REQUIRES_NEW,
			isolation = Isolation.READ_COMMITTED,
			noRollbackFor = {AccountBalanceException.class},
			readOnly = false,
			timeout = 3
			)
	@Override
	public void buy(String username, String isbn) {
	
		//1. 获取图书金额
		int price = this.bookStoreDao.getPriceByIsbn(isbn);
		
		//2. 图书库存减一
		this.bookStoreDao.updateStoreStock(isbn);
		
		//3. 用户金额减去图书金额
		this.bookStoreDao.updateUserBalance(username, price);
		
	}
}
