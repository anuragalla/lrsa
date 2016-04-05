package com.linearrsa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The persistent class for the users database table.
 * 
 * @author Anurag Alla
 */
@Entity
@Table(name = "message")
@XmlRootElement
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "message", length=3000)
	private String message;
	
	@Column(name = "type", length=45)
	private String type;
	
	@Column(name = "recieved_time", nullable = false)
	private Date recievedTime;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="from_user")
	private Users fromUser;

	//bi-directional many-to-one association to User
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="to_user")
	private Users toUser;


	public Message() {
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Users getFromUser() {
		return fromUser;
	}


	public void setFromUser(Users fromUser) {
		this.fromUser = fromUser;
	}


	public Users getToUser() {
		return toUser;
	}


	public void setToUser(Users toUser) {
		this.toUser = toUser;
	}
	
	public Date getRecievedTime() {
		return recievedTime;
	}

	public void setRecievedTime(Date recievedTime) {
		this.recievedTime = recievedTime;
	}



}
