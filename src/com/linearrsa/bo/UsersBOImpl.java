/**
 * @(#)UsersBOImpl.java  1.0   Dec 31, 2015
 * 
 * Copyright (c) 2014 PrimesoftInc.
 * All rights reserved.
 *
 */

package com.linearrsa.bo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.linearrsa.bo.UsersBO;
import com.linearrsa.common.ChurnyResourceBundle;
import com.linearrsa.common.ChurnyException;
import com.linearrsa.dao.UsersDAO;
import com.linearrsa.entity.Users;
import com.linearrsa.util.HibernateUtil;
import com.linearrsa.util.LinearRSA;
import com.linearrsa.util.PasswordHash;
import com.linearrsa.util.RSA;

/**
 * @author Anurag Alla
 *
 */
@Service("UsersBO")
@Scope(value = "prototype")
public class UsersBOImpl implements UsersBO {
	private static final Log log = LogFactory.getLog(UsersBOImpl.class);

	@Autowired
	private UsersDAO usersDAO;

	public void create(Users users) throws ChurnyException {
		try {
			if(users != null) {
				usersDAO.create(users);
				generateKeysForUser(users.getUserId(), "RSA");
				generateKeysForUser(users.getUserId(), "LinearRSA");
			}
		}
		catch(ChurnyException pcme) {
			throw pcme;
		}
		catch(Exception e) {
			log.error(ChurnyResourceBundle.getMessage("UsersSaveFailed"), e);
			throw new ChurnyException(ChurnyResourceBundle.getMessage("UsersSaveFailed"));
		}
	}

	public Users retrieveById(Integer usersId) throws ChurnyException {
		Users users = new Users();
		try {
			if(usersId == null || usersId.intValue() == 0) {
				throw new ChurnyException(ChurnyResourceBundle.getMessage("InvalidInput"));
			}
		//	users = usersDAO.findById(usersId);
			//hibernate proxy here
			HibernateUtil.removeProxies(users);
		}
		catch(ChurnyException pcme) {
			throw pcme;
		}
		catch(Exception e) {
			log.error(ChurnyResourceBundle.getMessage("UsersRetrieveByIdFailed"), e);
			throw new ChurnyException(ChurnyResourceBundle.getMessage("UsersRetrieveByIdFailed"));
		}
		return users;
	}
	
	@Override
	public Users checkLogin(Users users) throws ChurnyException {
		try {
//			PasswordHash hash = new PasswordHash();
//			users.setPassword(hash.hashToSHA1(users.getPassword()));
			users = usersDAO.checkLogin(users);
			HibernateUtil.removeProxies(users);
		}
		catch(ChurnyException pre) {
			throw pre;
		}
		catch(Exception e) {
			log.error(ChurnyResourceBundle.getMessage("CheckLoginFailed"), e);
			throw new ChurnyException(ChurnyResourceBundle.getMessage("AuthenticationFailed"));
		}
		return users;
	}

	public void delete(Integer usersId) throws ChurnyException {
		try {
			if(usersId != null && usersId.intValue() != 0) {
				usersDAO.delete(usersId);
			}
		}
		catch(ChurnyException pcme) {
			throw pcme;
		}
		catch(Exception e) {
			log.error(ChurnyResourceBundle.getMessage("UsersDeleteFailed"), e);
			throw new ChurnyException(ChurnyResourceBundle.getMessage("UsersDeleteFailed"));
		}
	}

	public List<Users> retrieveList() throws ChurnyException {
		List<Users> usersList = null;
		try {
			usersList = usersDAO.findAll();
			for(Users users : usersList) {
				HibernateUtil.removeProxies(users);
			}
		}
		catch(ChurnyException pcme) {
			throw pcme;
		}
		catch(Exception e) {
			log.error(ChurnyResourceBundle.getMessage("UsersRetrieveListFailed"), e);
			throw new ChurnyException(ChurnyResourceBundle.getMessage("UsersRetrieveListFailed"));
		}
		return usersList;
	}

	@Override
	public Users generateKeysForUser(Integer userId, String encryptionType) {
		Users users = new Users();
		
		try {
			users.setUserId(userId);
			if(encryptionType.equalsIgnoreCase("RSA")){
				RSA rsa = new RSA(1024);
				BigInteger pubKey = rsa.getKeys().get("pubKey");
				BigInteger privKey = rsa.getKeys().get("privKey");
//				Set<Entry<String, BigInteger>> m = rsa.getKeys().entrySet();
//				Iterator<Entry<String, BigInteger>> mapIterator = m.iterator();
//		        while (mapIterator.hasNext()) {
//		            Entry<String, BigInteger> mapEntry = mapIterator.next();
//		            String keyValue = mapEntry.getKey();
//		            BigInteger value = mapEntry.getValue();
//		            
//		        }
				users.setRsaPrivKey(privKey.toString());
				users.setRsaPubKey(pubKey.toString());
				users.setRsaNvalue(rsa.getKeys().get("nValue").toString());
			}
			if(encryptionType.equalsIgnoreCase("LinearRSA")){
				LinearRSA linearRSA = new LinearRSA(1024);
				BigInteger pubKey = linearRSA.getKeys().get("pubKey");
				BigInteger privKey = linearRSA.getKeys().get("privKey");
				users.setLinearRSAPrivKey(privKey.toString());
				users.setLinearRSAPubKey(pubKey.toString());
				users.setLinearNvalue(linearRSA.getKeys().get("nValue").toString());
			}
			users =usersDAO.generateKeysForUser(users,encryptionType);
			HibernateUtil.removeProxies(users);
//			HibernateUtil.removeProxies(users.getMessages1());
//			HibernateUtil.r
		} catch (Exception e) {
			// TODO: handle exception
		}
		return users;
	}

	@Override
	public List<String> encrypt(String encryptionType, Integer userId, String plaintext) {
		Users users = new Users();
		String cipherText = null;
		List<String> cipherData=new ArrayList<String>();
		try {
			users = usersDAO.findKeysByUser(encryptionType,userId);
			if(encryptionType.equalsIgnoreCase("RSA")){
				BigInteger pubKey = new BigInteger(users.getRsaPubKey());
				BigInteger privKey = new BigInteger(users.getRsaPrivKey());
				BigInteger nValue = new BigInteger(users.getRsaNvalue());
				RSA rsa = new RSA(nValue,pubKey);
				BigInteger pText = new BigInteger(plaintext.getBytes());
				
				//cipherText = rsa.encrypt(pText).toString();
				
				cipherData.add(0, rsa.encrypt(pText).toString());
				cipherData.add(1, String.valueOf(rsa.getTotalTime()));
			}
			if(encryptionType.equalsIgnoreCase("LinearRSA")){
				BigInteger pubKey = new BigInteger(users.getLinearRSAPubKey());
				BigInteger privKey = new BigInteger(users.getLinearRSAPrivKey());
				BigInteger nValue = new BigInteger(users.getLinearNvalue());
				LinearRSA linearRSA = new LinearRSA(nValue,pubKey);
				BigInteger pText = new BigInteger(plaintext.getBytes());
				
				linearRSA.encrypt(pText);

				//cipherText= linearRSA.getCipherTxt().toString();
				cipherData.add(0, linearRSA.getCipherTxt().toString());
				cipherData.add(1, String.valueOf(linearRSA.getTotalTime()));
			}
			HibernateUtil.removeProxies(users);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return cipherData;
	}

	@Override
	public void encryptAndSaveFile(InputStream uploadedInputStream, String storagePath, String fileName,Integer userId) {
		try {
			String uploadedFileLocation = storagePath+fileName;
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
			
			Users users = new Users();
			String encryptionType = "LinearRSA";
			users = usersDAO.findKeysByUser(encryptionType,userId);
			if(encryptionType.equalsIgnoreCase("LinearRSA")){
				BigInteger pubKey = new BigInteger(users.getLinearRSAPubKey());
				BigInteger privKey = new BigInteger(users.getLinearRSAPrivKey());
				BigInteger nValue = new BigInteger(users.getLinearNvalue());
				LinearRSA linearRSA = new LinearRSA(nValue,pubKey);
				linearRSA.encryptFile(storagePath, fileName);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
