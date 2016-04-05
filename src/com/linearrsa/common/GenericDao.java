package com.linearrsa.common;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface GenericDao<T> {

	List<T> findAll() throws Exception;

	List<T> retrieveList() throws Exception;

	T findById(Integer id) throws Exception;

	T retrieveById(Integer id) throws Exception;

	T findById(Class<T> c1ass, Object id) throws Exception;

	void save(T entity) throws Exception;

	void create(T entity) throws Exception;

	void create(T entity, Boolean autoCommit) throws Exception;

	void createAndCommit(T entity) throws Exception;

	void createAndNoCommit(T entity) throws Exception;

	void save(T entity, Boolean autoCommit) throws Exception;

	void insert(T entity) throws Exception;

	void insert(T entity, Boolean autoCommit) throws Exception;

	void saveOrUpdate(T entity) throws Exception;

	void saveOrUpdate(T entity, Boolean autoCommit) throws Exception;

	void saveAndCommit(T entity) throws Exception;

	void saveAndNoCommit(T entity) throws Exception;

	void update(T entity) throws Exception;

	void update(T entity, Boolean autoCommit) throws Exception;

	void updateAndCommit(T entity) throws Exception;

	void updateAndNoCommit(T entity) throws Exception;

	void delete(Object o) throws Exception;

	void delete(Object entity, Boolean autoCommit) throws Exception;

	void deleteAndCommit(Object entity) throws Exception;

	void deleteAndNoCommit(Object entity) throws Exception;

	void delete(Class<T> c1ass, Object o) throws Exception;
}
