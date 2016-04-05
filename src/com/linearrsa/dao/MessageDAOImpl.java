package com.linearrsa.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.linearrsa.common.ChurnyException;
import com.linearrsa.common.ChurnyResourceBundle;
import com.linearrsa.common.GenericDaoImpl;
import com.linearrsa.entity.Message;
import com.linearrsa.entity.Users;

@Component("messageDAO")
@Scope(value = "prototype")
public class MessageDAOImpl extends GenericDaoImpl<Message> implements MessageDAO{
	private static final Log log = LogFactory.getLog(MessageDAOImpl.class);
	@Override
	public List<Message> retrieveRecievedMessages(Integer fromId,Integer toId,String eType) throws ChurnyException {
		List<Message> messages = null;
		try {
			String query = "select m from Message m join fetch m.fromUser f join fetch m.toUser t  where f.userId = :fromId and t.userId = :toId and  type = :eType";
			Query q = entityManager.createQuery(query);
			List<Integer> id = new ArrayList<Integer>();
			id.add(toId);
			id.add(fromId);
			q.setParameter("fromId", fromId);
			q.setParameter("toId", toId);
			q.setParameter("eType", eType);
			messages=q.getResultList();
		} catch (NoResultException nre) {
			log.error(ChurnyResourceBundle.getMessage("NoResultFound"));
			throw new ChurnyException(ChurnyResourceBundle.getMessage("AuthenticationFailed"));
		} catch (Exception e) {
			log.error(ChurnyResourceBundle.getMessage("recieved messages"), e);
			throw new ChurnyException(ChurnyResourceBundle.getMessage("AuthenticationFailed"));
		}
		
		return messages;
	}

}
