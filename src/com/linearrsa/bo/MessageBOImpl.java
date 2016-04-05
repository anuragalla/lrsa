package com.linearrsa.bo;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.linearrsa.common.ChurnyException;
import com.linearrsa.common.ChurnyResourceBundle;
import com.linearrsa.dao.MessageDAO;
import com.linearrsa.entity.Message;
import com.linearrsa.util.HibernateUtil;
import com.linearrsa.util.LinearRSA;
import com.linearrsa.util.RSA;

@Service("MessageBO")
@Scope(value = "prototype")
public class MessageBOImpl implements MessageBO{

	public MessageBOImpl() {
		// TODO Auto-generated constructor stub
	}
	
	private static final Log log = LogFactory.getLog(MessageBOImpl.class);

	@Autowired
	private MessageDAO messageDAO;

	public void create(Message message) throws ChurnyException {
		try {
			if(message != null) {
				message.setRecievedTime(new Date());
				messageDAO.create(message);
			}
		}
		catch(ChurnyException pcme) {
			throw pcme;
		}
		catch(Exception e) {
			log.error(ChurnyResourceBundle.getMessage("MessageSaveFailed"), e);
			throw new ChurnyException(ChurnyResourceBundle.getMessage("MessageSaveFailed"));
		}
	}

	public Message retrieveById(Integer messageId) throws ChurnyException {
		Message message = new Message();
		try {
			if(messageId == null || messageId.intValue() == 0) {
				throw new ChurnyException(ChurnyResourceBundle.getMessage("InvalidInput"));
			}
		//	message = messageDAO.findById(messageId);
			//hibernate proxy here
			HibernateUtil.removeProxies(message);
		}
		catch(ChurnyException pcme) {
			throw pcme;
		}
		catch(Exception e) {
			log.error(ChurnyResourceBundle.getMessage("MessageRetrieveByIdFailed"), e);
			throw new ChurnyException(ChurnyResourceBundle.getMessage("MessageRetrieveByIdFailed"));
		}
		return message;
	}
	
	

	public void delete(Integer messageId) throws ChurnyException {
		try {
			if(messageId != null && messageId.intValue() != 0) {
				messageDAO.delete(messageId);
			}
		}
		catch(ChurnyException pcme) {
			throw pcme;
		}
		catch(Exception e) {
			log.error(ChurnyResourceBundle.getMessage("MessageDeleteFailed"), e);
			throw new ChurnyException(ChurnyResourceBundle.getMessage("MessageDeleteFailed"));
		}
	}

	public List<Message> retrieveList() throws ChurnyException {
		List<Message> messageList = null;
		try {
			messageList = messageDAO.findAll();
			for(Message message : messageList) {
				HibernateUtil.removeProxies(message);
			}
		}
		catch(ChurnyException pcme) {
			throw pcme;
		}
		catch(Exception e) {
			log.error(ChurnyResourceBundle.getMessage("MessageRetrieveListFailed"), e);
			throw new ChurnyException(ChurnyResourceBundle.getMessage("MessageRetrieveListFailed"));
		}
		return messageList;
	}

	@Override
	public List<Message> retrieveRecievedMessages(Integer fromId,Integer toId,String eType) throws ChurnyException {
		List<Message> messageList = null;
		try {
			messageList = messageDAO.retrieveRecievedMessages(fromId,toId,eType);
			for(Message message : messageList) {
				HibernateUtil.initialize(message.getToUser());
				HibernateUtil.initialize(message.getFromUser());
				HibernateUtil.removeProxies(message.getFromUser());
				HibernateUtil.removeProxies(message.getToUser());
				HibernateUtil.removeProxies(message);
			}
			if(eType.equalsIgnoreCase("RSA")){
				for(Message message:messageList){
					RSA rsa = new RSA();
					rsa.setN(new BigInteger(message.getToUser().getRsaNvalue()));
					rsa.setD(new BigInteger(message.getToUser().getRsaPrivKey()));
					BigInteger pText = rsa.decrypt(new BigInteger(message.getMessage()));
					String pString = new String(pText.toByteArray());
					message.setMessage(pString);
					
				}
			}
			if (eType.equalsIgnoreCase("LinearRSA")) {
				for(Message message:messageList){
					LinearRSA linearRsa = new LinearRSA();
					linearRsa.setN(new BigInteger(message.getToUser().getLinearNvalue()));
					linearRsa.setQe(new BigInteger(message.getToUser().getLinearRSAPrivKey()));
					BigInteger pText = linearRsa.decrypt1(new BigInteger(message.getMessage()));
					String pString = new String(pText.toByteArray());
					message.setMessage(pString);
					
				}
			}
		}
		catch(ChurnyException pcme) {
			throw pcme;
		}
		catch(Exception e) {
			log.error(ChurnyResourceBundle.getMessage("MessageRetrieveListFailed"), e);
			throw new ChurnyException(ChurnyResourceBundle.getMessage("MessageRetrieveListFailed"));
		}
		return messageList;
	}


}
