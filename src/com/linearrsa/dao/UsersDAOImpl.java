/**
 * @(#)UsersDAOImpl.java  1.0 Dec 31, 2015
 *
 * Copyright (c) 2013 PrimesoftInc.
 * All rights reserved.
 *
 */

package com.linearrsa.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.linearrsa.common.ChurnyException;
import com.linearrsa.common.ChurnyResourceBundle;
import com.linearrsa.common.GenericDaoImpl;
import com.linearrsa.dao.UsersDAO;
import com.linearrsa.entity.Users;

/**
 * A data access object (Dao) providing persistence and search support for Users
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see com.churny.entity.Users
 * @author Anurag Alla
 */

@Component("usersDAO")
@Scope(value = "prototype")
public class UsersDAOImpl extends GenericDaoImpl<Users> implements UsersDAO {
	private static final Log log = LogFactory.getLog(UsersDAOImpl.class);

	@Override
	public Users checkLogin(Users users) throws ChurnyException {
		try {
			String queryString = "select u from Users u where u.email = :email and u.password = :password";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("email", users.getEmail());
			query.setParameter("password", users.getPassword());
			users = (Users) query.getSingleResult();
		} catch (NoResultException nre) {
			log.error(ChurnyResourceBundle.getMessage("NoResultFound"));
			throw new ChurnyException(ChurnyResourceBundle.getMessage("AuthenticationFailed"));
		} catch (Exception e) {
			log.error(ChurnyResourceBundle.getMessage("CheckLoginFailed"), e);
			throw new ChurnyException(ChurnyResourceBundle.getMessage("AuthenticationFailed"));
		}
		return users;
	}

	@Override
	public Users generateKeysForUser(Users users, String encryptionType) {

		try {
			if (encryptionType.equalsIgnoreCase("RSA")) {
				String queryString = "update Users u set u.rsaPrivKey = :rsaPrivKey, u.rsaPubKey = :rsaPubKey,u.nvalue = :rsaNvalue where u.userId = :id";
				Query query = entityManager.createQuery(queryString);
				query.setParameter("rsaPrivKey", users.getRsaPrivKey());
				query.setParameter("rsaPubKey", users.getRsaPubKey());
				query.setParameter("rsaNvalue", users.getRsaNvalue());
				query.setParameter("id", users.getUserId());
				query.executeUpdate();
			}
			if (encryptionType.equalsIgnoreCase("LinearRSA")) {
				String queryString = "update Users u set u.linearRSAPrivKey = :linearRSAPrivKey, u.linearRSAPubKey = :linearRSAPubKey,u.linearNvalue = :linearNvalue where u.userId = :id";
				Query query = entityManager.createQuery(queryString);
				query.setParameter("linearRSAPrivKey", users.getLinearRSAPrivKey());
				query.setParameter("linearRSAPubKey", users.getLinearRSAPubKey());
				query.setParameter("linearNvalue", users.getLinearNvalue());
				query.setParameter("id", users.getUserId());
				query.executeUpdate();
			}
			String queryString = "select u from Users u where u.userId = :id";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("id", users.getUserId());
			users = (Users) query.getSingleResult();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return users;
	}

	@Override
	public Users findKeysByUser(String encryptionType, Integer userId) throws ChurnyException {
		Users users = new Users();
		try {
			String queryString = "select u from Users u where u.userId = :id";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("id", userId);
			users = (Users) query.getSingleResult();
		} catch (NoResultException nre) {
			log.error(ChurnyResourceBundle.getMessage("NoResultFound"));
			throw new ChurnyException(ChurnyResourceBundle.getMessage("AuthenticationFailed"));
		} catch (Exception e) {
			log.error(ChurnyResourceBundle.getMessage("CheckLoginFailed"), e);
			throw new ChurnyException(ChurnyResourceBundle.getMessage("AuthenticationFailed"));
		}
		return users;
	}
}
