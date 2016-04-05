/**
 * @(#)HibernateUtil.java  1.0   May 3, 2011
 * 
 * Copyright (c) 2010 Greenclinical, Inc.
 * All rights reserved.
 *
 */

package com.linearrsa.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.collection.AbstractPersistentCollection;
import org.hibernate.collection.PersistentBag;
//import org.hibernate.lob.SerializableBlob;
import org.hibernate.proxy.HibernateProxy;

//import com.greenclinical.common.CommonCore;

public class HibernateUtil {
	private static final Log log = LogFactory.getLog(HibernateUtil.class);
	/*
	 * This method will remove all proxies and replace with their target object irrespective of their initialization.
	 */
	@SuppressWarnings("unchecked")
	public static void removeProxies(Object object) throws Exception{
		if(object == null){
			return;
		}
//		if(!(object instanceof CommonCore)){
//			log.debug("proxied class : "+object.getClass().getName());
//			throw new EMRException("com.greenclinicals.common.CommonCore not found");
//		}
//		else 
		if(object instanceof HibernateProxy){
			log.debug("Proxied class : "+object.getClass().getName());
			throw new Exception("Cannot remove proxies of a proxied class");
		}
//		else{
//			CommonCore commonCore = (CommonCore)object;
//			//	Mark the object as traversed
//			commonCore.setUploaded(true);
//		}
		Object targetObject = null;
		Class objClass = object.getClass();
		try {
			Method[] methodArray = objClass.getDeclaredMethods();
			for(int i=0; i<methodArray.length; i++) {
				String args[] = null;
				//	Invoke all get methods in the given object
				if(methodArray[i].getName().indexOf("get") == 0) {
					//	Excluding primitive data types(int, float, long, boolean ...)
					if(methodArray[i].invoke(object, args) instanceof Object) {
						Object returnedObject = methodArray[i].invoke(object, args);
						//	If returned object is a proxy then remove proxy
						if(returnedObject instanceof HibernateProxy ) {
							HibernateProxy hibernateProxy = (HibernateProxy)returnedObject;
							if(hibernateProxy.getHibernateLazyInitializer().getSession() != null){
								//	If proxy is initialized get target object or return null
								targetObject = hibernateProxy.getHibernateLazyInitializer().getImplementation(hibernateProxy.getHibernateLazyInitializer().getSession());
								
								//	Find corresponding set method 
								String setMethodName = methodArray[i].getName().replaceFirst("get", "set");
								Class parameterTypes[] = new Class[1];
								parameterTypes[0] = Class.forName(hibernateProxy.getHibernateLazyInitializer().getEntityName());
								//	Invoke corresponding set method with returned target object of get method 
								//	to override proxy with target object 
								objClass.getMethod(setMethodName, parameterTypes).invoke(object, targetObject);
								//	Removes proxies in target object so that this function can be recursive.
								HibernateUtil.removeProxies(targetObject);
//								CommonCore commonCore1 = (CommonCore)targetObject;
//								if(commonCore1 != null){
//									commonCore1.setUploaded(false);
//								}
							}
						}
						else if(returnedObject instanceof AbstractPersistentCollection) {
							AbstractPersistentCollection abstractPersistentCollection = (AbstractPersistentCollection)returnedObject;
							String setMethodName = methodArray[i].getName().replaceFirst("get", "set");
							Class parameterTypes[] = new Class[1];
							if(abstractPersistentCollection.getValue().getClass().equals(org.hibernate.collection.PersistentBag.class)) {
								parameterTypes[0] = java.util.List.class;
								if(abstractPersistentCollection.getStoredSnapshot() == null) {
									java.util.List nullList = null;
									objClass.getMethod(setMethodName, parameterTypes).invoke(object, nullList);
								}
								else {
									PersistentBag persistentBag = (PersistentBag)abstractPersistentCollection.getValue();
									List dataList = new ArrayList();
									for(int persistentBagCnt=0; persistentBagCnt<persistentBag.size(); persistentBagCnt++) {
										if(persistentBag.get(persistentBagCnt) instanceof HibernateProxy){
											HibernateProxy hibernateProxy = (HibernateProxy)persistentBag.get(persistentBagCnt);
											//If proxy is initialized get target object or return null
											targetObject = hibernateProxy.getHibernateLazyInitializer().getImplementation(hibernateProxy.getHibernateLazyInitializer().getSession());
											//If proxy is initialized then only add to list
											if(targetObject != null){
												dataList.add(targetObject);
												HibernateUtil.removeProxies(targetObject);
											}
										}
										else{
											dataList.add(persistentBag.get(persistentBagCnt));
											HibernateUtil.removeProxies(persistentBag.get(persistentBagCnt));
										}
									}
									objClass.getMethod(setMethodName, parameterTypes).invoke(object, dataList);
									
								}
							}
							//	TODO Set is not traversed as we don't use java.util.Set anywhere in our project
							else if(abstractPersistentCollection.getValue().getClass().equals(org.hibernate.collection.PersistentSet.class)) {
								parameterTypes[0] = java.util.Set.class;
								if(abstractPersistentCollection.getStoredSnapshot() == null) {
									java.util.Set nullSet = null;
									objClass.getMethod(setMethodName, parameterTypes).invoke(object, nullSet);
								}
								
							}
						}
						//	Ignore all wrapper classes as they don't have any proxies inside it
						else if(returnedObject instanceof Integer || returnedObject instanceof Float
								|| returnedObject instanceof Double || returnedObject instanceof String
								|| returnedObject instanceof Boolean || returnedObject instanceof Long
								|| returnedObject instanceof Byte || returnedObject instanceof Character
								|| returnedObject instanceof Short || returnedObject instanceof java.util.Date
								|| returnedObject instanceof java.sql.Date || returnedObject instanceof java.sql.Time
								|| returnedObject instanceof java.sql.Timestamp) {
	
							//	Do nothing.
						} 
						//	Removing proxies of collections
						else if(returnedObject instanceof java.util.Collection){
							Iterator iterator = ((java.util.Collection)returnedObject).iterator();
							while(iterator.hasNext()) {
								HibernateUtil.removeProxies(iterator.next());
							}   
						}
						else if(returnedObject instanceof Object) {
							String setMethodName = methodArray[i].getName().replaceFirst("get", "set");
							Class parameterTypes[] = new Class[1];
							parameterTypes[0] = returnedObject.getClass();
							//	Invoke corresponding set method with returned target object of get method 
							//	to override proxy with target object 
							
							/*
							 *  "if" condition written by Gowtham on 10th April 2014
							 */
							if(object.equals(targetObject)) {
								objClass.getMethod(setMethodName, parameterTypes).invoke(object, targetObject);
							}
							//	Removes proxies in target object so that this function can be recursive.
							HibernateUtil.removeProxies(targetObject);
						}
//						else if(returnedObject instanceof SerializableBlob){
//							// TODO 
//						}
						//	Removing proxies of a user defined objects
//						else if(returnedObject instanceof CommonCore){
//							CommonCore coreCommon = (CommonCore)returnedObject;
//							if(coreCommon.getUploaded().booleanValue() == false) {
//								//	Mark the object as traversed
//								coreCommon.setUploaded(true);
//								HibernateUtil.removeProxies(returnedObject);
//								//	Again mark it as not traversed as the same object may appear as subling
//								coreCommon.setUploaded(false);
//							}
//							//	If the same object appears again, set it as null to avoid recursion. 
//							else {
//								String setMethodName = methodArray[i].getName().replaceFirst("get", "set");
//								Class parameterTypes[] = new Class[1];
//								parameterTypes[0] = returnedObject.getClass();
//								Object object2 = null;
//								objClass.getMethod(setMethodName, parameterTypes).invoke(object, object2);
//							}
//
//						}
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			log.error("Failed in the remove proxies method ", e);
			throw new Exception(e);
		}
	}
	
//	public static Session getCurrentSession(HibernateTemplate hibernateTemplate)throws EMRException{
//		try{
//			return hibernateTemplate.getSessionFactory().getCurrentSession();
//		}
//		catch(Exception e){
//			log.error("", e);
//			throw new EMRException(e);
//		}
//	}

	public static void initialize(Object object) {
		try {
			Hibernate.initialize(object);
			/**
			 * if(object instanceof CommonCore) {
				Class objClass = object.getClass();
				Method[] methodArray = objClass.getDeclaredMethods();
				String args[] = null;
				for(int methodArrayCounter=0; methodArrayCounter<methodArray.length; methodArrayCounter++) {
					if(methodArray[methodArrayCounter].getReturnType().equals(java.lang.Integer.class)) {
						methodArray[methodArrayCounter].invoke(object, args);
						return;
					}
				}
			}
			else if(object instanceof java.util.Collection) {
				java.util.Collection collection = (Collection) object;
				collection.size();
			}
			*/
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
