/**
 * @(#)UsersDAO.java  1.0   Dec 31, 2015
 *
 * Copyright (c) 2013 PrimesoftInc.
 * All rights reserved.
 *
 */

package com.linearrsa.dao;

import java.util.List;

import com.linearrsa.common.ChurnyException;
import com.linearrsa.common.GenericDao;
import com.linearrsa.entity.Users;

/**
 * @author Anurag Alla
 *
 */
public interface UsersDAO extends GenericDao<Users> {
	public Users checkLogin(Users users) throws ChurnyException;

	public Users generateKeysForUser(Users users, String encryptionType);

	public Users findKeysByUser(String encryptionType, Integer userId)  throws ChurnyException;

}
