/**
 * @(#)UsersBO.java  1.0   Dec 31, 2015
 * 
 * Copyright (c) 2014 PrimesoftInc.
 * All rights reserved.
 *
 */

package com.linearrsa.bo;

import java.io.InputStream;
import java.util.List;

import com.linearrsa.common.ChurnyException;
import com.linearrsa.entity.Users;

/**
 * @author Anurag Alla
 *
 */
public interface UsersBO {
	void create(Users users) throws ChurnyException;
	Users retrieveById(Integer usersId) throws ChurnyException;
	void delete(Integer usersId) throws ChurnyException;
	List<Users> retrieveList() throws ChurnyException;
	Users checkLogin(Users users) throws ChurnyException;
	Users generateKeysForUser(Integer userId, String encryptionType);
	List<String> encrypt(String encryptionType, Integer userId, String plaintext);
	void encryptAndSaveFile(InputStream uploadedInputStream, String storagePath, String fileName,Integer userId);
}
