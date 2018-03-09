package com.zc.cris.SpringTransaction.service;

import java.util.List;

public interface UserService {

	// 用户购买多本书
	void buyList(String username, List<String> isbns);
}
