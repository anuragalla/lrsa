package com.linearrsa.bo;

import java.util.List;

import com.linearrsa.common.ChurnyException;
import com.linearrsa.entity.Message;

public interface MessageBO {
	void create(Message message) throws ChurnyException;
	Message retrieveById(Integer Id) throws ChurnyException;
	void delete(Integer Id) throws ChurnyException;
	List<Message> retrieveList() throws ChurnyException;
	List<Message> retrieveRecievedMessages(Integer fromId,Integer toId,String eType) throws ChurnyException;
}
