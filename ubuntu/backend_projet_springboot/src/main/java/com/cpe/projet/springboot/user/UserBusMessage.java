package com.cpe.projet.springboot.user;

import java.io.Serializable;

public class UserBusMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2541629527081286702L;
	private UserDTO user;
	private String functionName;
	private String id;

	
	public UserBusMessage() {
    }

	
	public UserBusMessage(UserDTO user,String functionName) {
		this.user = user;
		this.functionName=functionName;
		this.id=null;
	}
	
	
	public UserBusMessage(String id, String functionName) {
		this.user = null;
		this.functionName=functionName;
		this.id=id;
	}

	public String getFunctionName() {
		return functionName;
	}

	public UserDTO getUser() {
		return user;
	}

	public String getId() {
		return id;
	}
	
	@Override
    public String toString() {
        return functionName;
    }

	
	
}
