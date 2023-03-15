package com.cpe.projet.springboot.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;





public class UserDTO implements Serializable {

	private static final long serialVersionUID = 10692754582176L;
	private Integer id;
	private String login;
	private String pwd;
	private String lastName;
	private String surName;
	private String email;
	
	
	public UserDTO() {
	}

	public UserDTO(UserModel user) {
		this.id = user.getId();
		this.login = user.getLogin();
		this.pwd = user.getPwd();
		this.lastName = user.getLastName();
		this.surName = user.getSurName();
		this.email = user.getEmail();
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}


	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
