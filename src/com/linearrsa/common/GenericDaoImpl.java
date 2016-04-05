package com.linearrsa.common;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Component;


@Component
public abstract class GenericDaoImpl<T> implements GenericDao<T> {
	private Class<T> type;

	@PersistenceContext(unitName="PERSISTENCE_UNIT")
	public EntityManager entityManager;


	@SuppressWarnings("unchecked")
	public GenericDaoImpl() {
		ParameterizedType pt = (ParameterizedType) getClass()
				.getGenericSuperclass();
		type = (Class<T>) pt.getActualTypeArguments()[0];
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() throws Exception {
		final StringBuilder sb = new StringBuilder("SELECT o from ");
		sb.append(type.getSimpleName()).append(" o ");
		return entityManager.createQuery(sb.toString()).getResultList();
	}

	public List<T> retrieveList() throws Exception {
		return this.findAll();
	}
//changed here to byte[]
	public T findById(final Integer id) throws Exception {
		return entityManager.find(type, id);
	}
	//changed here to byte[]
	public T retrieveById(Integer id) throws Exception {
		return this.findById(id);
	}

	public T findById(Class<T> c1ass, Object id) throws Exception {
		return entityManager.find(c1ass, id);
	}

	public void save(final T entity) throws ConstraintFailureException, Exception {
		/*
		 * committing db transaction by default
		 */
		this.saveAndCommit(entity);
	}

	public void save(T entity, Boolean autoCommit) throws Exception {
		if (autoCommit)
			this.saveAndCommit(entity);
		else
			this.saveAndNoCommit(entity);
	}

	public void insert(final T entity) throws Exception {
		this.save(entity);
	}

	public void insert(T entity, Boolean autoCommit) throws Exception {
			this.save(entity, autoCommit);
	}

	public void saveAndCommit(T entity) throws ConstraintFailureException, Exception {
		try {
			entityManager.persist(entity);
		}
		catch(EntityExistsException eee) {
			Throwable throwable = eee.getCause();
			if(throwable instanceof ConstraintViolationException) {
				ConstraintViolationException cve = (ConstraintViolationException) throwable;
				if(cve.getErrorCode() == 1062)
					throw new ConstraintFailureException("Constraint failure exception");
			}
		}
		catch(PersistenceException pe) {
//			pe.printStackTrace();
			Throwable throwable = pe.getCause();
			if(throwable instanceof ConstraintViolationException) {
				ConstraintViolationException cve = (ConstraintViolationException) throwable;
				if(cve.getErrorCode() == 1062){
					throw new ConstraintFailureException("Constraint failure exception");
//					throw new ConstraintFailureException(PRResourceBundle.getMessage("AlreadyExists"));
				}
			}
		}
		catch(Exception e) {
			try {
				e.printStackTrace();
			}
			catch(Exception ee) {
				// do nothing
			}
			throw e;
		}
	}

	public void saveAndNoCommit(T entity) throws Exception {
		entityManager.persist(entity);
	}

	public void delete(Class<T> c1ass, Object id) throws Exception {
		T entity = null;
		try {
			entity = entityManager.find(c1ass, id);
		} catch (Exception e) {
			// throw new Exception(e);
		}
		if (entity != null)
			entityManager.remove(entity);
	}

	public void delete(final Object id) throws Exception {
		this.deleteAndCommit(id);
	}

	public void delete(Object id, Boolean autoCommit) throws Exception {
		if (autoCommit)
			this.deleteAndCommit(id);
		else
			this.deleteAndNoCommit(id);
	}

	public void deleteAndCommit(Object id) throws Exception {
		T entity = null;
		try {
			entity = entityManager.find(type, id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		if (entity != null) {
			try {
				entityManager.remove(entity);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	public void deleteAndNoCommit(Object id) throws Exception {
		this.delete(type, id);
	}

	public void update(final T entity) throws Exception {
		this.updateAndCommit(entity);
	}

	public void update(T entity, Boolean autoCommit) throws Exception {
		if (autoCommit)
			this.updateAndCommit(entity);
		else
			this.updateAndNoCommit(entity);
	}

	public void saveOrUpdate(final T entity) throws Exception {
		this.updateAndCommit(entity);
	}

	public void saveOrUpdate(T entity, Boolean autoCommit) throws Exception {
		if (autoCommit)
			this.updateAndCommit(entity);
		else
			this.updateAndNoCommit(entity);
	}

	public void updateAndCommit(T entity) throws Exception {
		try {
			entityManager.merge(entity);
		} 
		catch (Exception e) {
			try {
				entityManager.getTransaction().rollback();
			} catch (Exception ee) {
				// do nothing
			}
			throw e;
		}
	}

	public void updateAndNoCommit(T entity) throws Exception {
		entityManager.merge(entity);
	}

	public void create(final T entity) throws ConstraintFailureException, Exception {
		this.save(entity);
	}

	public void create(T entity, Boolean autoCommit) throws Exception {		
			this.save(entity, autoCommit);
	}

	public void createAndCommit(T entity) throws Exception {
		this.saveAndCommit(entity);
	}

	public void createAndNoCommit(T entity) throws Exception {
		this.saveAndNoCommit(entity);
	}
}
