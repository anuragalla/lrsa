package com.linearrsa.dao;

import java.util.List;

import com.linearrsa.common.ChurnyException;
import com.linearrsa.common.GenericDao;
import com.linearrsa.entity.Message;

public interface MessageDAO extends GenericDao<Message>{
	public List<Message> retrieveRecievedMessages(Integer fromId,Integer toId,String eType) throws ChurnyException;
}
