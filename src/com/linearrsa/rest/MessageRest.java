package com.linearrsa.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linearrsa.bo.MessageBO;
import com.linearrsa.entity.Message;

@Component
@Path("/message")
public class MessageRest {

	private static final Log log = LogFactory.getLog(MessageRest.class);
	private Message message;
	@Autowired
	private MessageBO messageBO;

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Message create(Message message) {
		try {
			messageBO.create(message);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	@POST
	@Path("/retrieve")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Message retrieveById(Message message) {
		try {
			message = messageBO.retrieveById(message.getId());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public void delete() {
		try {
			messageBO.delete(message.getId());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("/retrievelist")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> retrieveList() {
		List<Message> messageList = new ArrayList<Message>();
		try {
			messageList = messageBO.retrieveList();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return messageList;
	}
	
	@POST
	@Path("/show/{fromUser}/{toUser}/{eType}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> retrieveRecievedMessages(@PathParam("fromUser") Integer fromId,@PathParam("toUser") Integer toId
			,@PathParam("eType") String eType) {
		List<Message> messages = null;
		try {
			messages = messageBO.retrieveRecievedMessages(fromId,toId,eType);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return messages;
	}

}
