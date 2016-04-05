/**
 * @(#)UsersRest.java  1.0   Dec 31, 2015
 * 
 * Copyright (c) 2013 PrimesoftInc.
 * All rights reserved.
 *
 */

package com.linearrsa.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
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

import com.linearrsa.bo.UsersBO;
import com.linearrsa.common.ChurnyException;
import com.linearrsa.common.ChurnyResourceBundle;
import com.linearrsa.entity.Users;
import com.linearrsa.util.LinearRSA;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

/**
 *  @author Anurag Alla
 */
@Component
@Path("/users")
public class UsersRest {
	private static final Log log = LogFactory.getLog(UsersRest.class);
	private Users users;
	@Autowired
	private UsersBO usersBO;

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Users create(Users users) {
		try {
			usersBO.create(users);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	@POST
	@Path("/retrieve")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Users retrieveById(Users users) {
		try {
			users = usersBO.retrieveById(users.getUserId());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public void delete() {
		try {
			usersBO.delete(users.getUserId());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("/retrievelist")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Users> retrieveList() {
		List<Users> usersList = new ArrayList<Users>();
		try {
			usersList = usersBO.retrieveList();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return usersList;
	}
	
	@POST
	@Path("/checklogin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkLogin(Users users) {
		try {
			users = usersBO.checkLogin(users);
		}
		catch(ChurnyException pre) {
			log.error(ChurnyResourceBundle.getMessage("AuthenticationFailed"));
			return Response.status(2000).entity(pre.getMessage()).build();
		}
		catch(Exception e) {
			log.error(ChurnyResourceBundle.getMessage("CheckLoginFailed"), e);
		}
		return Response.ok().entity(users).build();
	}
	
	@POST
	@Path("/generatekeys/{eType}/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Users generateKeys(@PathParam("userId") Integer userId,@PathParam("eType") String eType) {
		try {
			users = usersBO.generateKeysForUser(userId, eType);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return users;
	}
	
	@POST
	@Path("/encrypt/{eType}/{userId}/{message}")
	@Produces(MediaType.APPLICATION_JSON)
	public Users encrypt(@PathParam("eType") String encryptionType,@PathParam("userId") Integer userId, @PathParam("message") String plaintext) {
		List<String> cipherData=null;
		String cipherText= null;
		Users u= new Users();;
		try {
			cipherData = usersBO.encrypt(encryptionType,userId, plaintext);
//			cipherText = encryptionType+" "+cipherText;
			u.setSysTime(cipherData.get(1));
			u.setRsaNvalue(cipherData.get(0));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return u;
	}
	
	@POST
	@Path("/upload/{userId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
		@FormDataParam("file") InputStream uploadedInputStream,
		@FormDataParam("file") FormDataContentDisposition fileDetail,
		@PathParam("userId") Integer userId) {
		String storagePath = "c://users//anurag//desktop//uploaded//";
		String uploadedFileLocation = storagePath + fileDetail.getFileName();
		
		usersBO.encryptAndSaveFile(uploadedInputStream,storagePath,fileDetail.getFileName(),userId);

		// save it
//		writeToFile(uploadedInputStream, uploadedFileLocation);
//
//		String output = "File uploaded to : " + uploadedFileLocation;
//		
//		String fileName = fileDetail.getFileName();
		
		return Response.status(200).build();

	}

	private void writeToFile(InputStream uploadedInputStream,
			String uploadedFileLocation) {

			try {
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
				
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
	
}