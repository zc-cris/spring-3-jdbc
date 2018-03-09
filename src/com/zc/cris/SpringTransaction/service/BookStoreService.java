package com.zc.cris.SpringTransaction.service;

import java.util.List;

public interface BookStoreService {

	
	
	//用户购买一本图书
	void buy (String username, String isbn);

	void buyList(String username, List<String> isbns);
	
}
