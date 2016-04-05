package com.linearrsa.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;



/**
 * The persistent class for the users database table.
 *  @author Anurag Alla
 */
@Entity
@Table(name="users")
@XmlRootElement
public class Users implements Serializable {
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", unique=true, nullable=false)
	private Integer userId;

	@Column(name="name")
	private String name;

	@Column(name="pwd",length=255)
	private String password;

	@Column(name="email",length=45)
	private String email;

	@Column(name="pub_key", length=2048)
	private String linearRSAPubKey;
	
	@Column(name="priv_key", length=2048)
	private String linearRSAPrivKey;

	@Column(name="rsa_pub_key", length=2048)
	private String rsaPubKey;
	
	@Column(name="rsa_priv_key", length=2048)
	private String rsaPrivKey;
	
	@Column(name="rsa_nvalue", length=3000)
	private String rsaNvalue;
	
	@Column(name="linear_nvalue", length=3000)
	private String linearNvalue;
	
	@Transient
	private String sysTime;
	
//	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy="users")
//	private List<Message> messages;
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy="fromUser")
	private List<Message> messages1;

	//bi-directional many-to-one association to Preset
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy="toUser")
	private List<Message> messages2;
	
	public Users() {
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLinearRSAPubKey() {
		return linearRSAPubKey;
	}

	public void setLinearRSAPubKey(String linearRSAPubKey) {
		this.linearRSAPubKey = linearRSAPubKey;
	}

	public String getLinearRSAPrivKey() {
		return linearRSAPrivKey;
	}

	public void setLinearRSAPrivKey(String linearRSAPrivKey) {
		this.linearRSAPrivKey = linearRSAPrivKey;
	}

	public String getRsaPubKey() {
		return rsaPubKey;
	}

	public void setRsaPubKey(String rsaPubKey) {
		this.rsaPubKey = rsaPubKey;
	}

	public String getRsaPrivKey() {
		return rsaPrivKey;
	}

	public void setRsaPrivKey(String rsaPrivKey) {
		this.rsaPrivKey = rsaPrivKey;
	}
	
	
	public String getRsaNvalue() {
		return rsaNvalue;
	}

	public void setRsaNvalue(String rsaNvalue) {
		this.rsaNvalue = rsaNvalue;
	}

	public String getLinearNvalue() {
		return linearNvalue;
	}

	public void setLinearNvalue(String linearNvalue) {
		this.linearNvalue = linearNvalue;
	}

	
	
	public String getSysTime() {
		return sysTime;
	}

	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}

	public List<Message> getMessages1() {
		return messages1;
	}

	public void setMessages1(List<Message> messages1) {
		this.messages1 = messages1;
	}

	public List<Message> getMessages2() {
		return messages2;
	}

	public void setMessages2(List<Message> messages2) {
		this.messages2 = messages2;
	}

}