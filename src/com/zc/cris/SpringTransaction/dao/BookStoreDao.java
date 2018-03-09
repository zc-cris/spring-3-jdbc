package com.zc.cris.SpringTransaction.dao;


public interface BookStoreDao {
	
	//根据书的isbn号获取书的单价
	int getPriceByIsbn(String isbn);
	
	//更新书的库存，使书号对应的库存量-1
	void updateStoreStock(String isbn);
	
	//更新用户的账户余额，使username对应的balance减去购书金额
	void updateUserBalance(String userName, int price);

}
