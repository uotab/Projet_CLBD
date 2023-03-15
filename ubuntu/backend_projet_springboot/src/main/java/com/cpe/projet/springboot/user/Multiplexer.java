package com.cpe.projet.springboot.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;





@Component
public class Multiplexer {
	
	@Autowired
	BusService busService;
	
	private final UserService userService;
	
	public Multiplexer(UserService userService, BusService busService) {
		this.busService=busService;
		this.userService=userService;
	}

	public void deleteUserBusService(String id) {
		UserBusMessage userBusMessage = new UserBusMessage(id,"deleteUser");
		//String jsonString = mapper.writeValueAsString(user);
		busService.sendMsg(userBusMessage);
		
	}
	
//	public void deleteUserBusService(String id,String busName) {
//		// TODO Auto-generated method stub
//		busService.sendMsg("deleteUser/"+id,busName);
//		
//	}

	public List<UserModel> getUserByLoginPwd(String username, String password) {
		// TODO Auto-generated method stub
		return userService.getUserByLoginPwd(username,password);
	}

	public boolean updateUserBusService(UserDTO user) {
		//ObjectMapper mapper = new ObjectMapper();
		try {
			UserBusMessage userBusMessage = new UserBusMessage(user,"updateUser");
			//String jsonString = mapper.writeValueAsString(user);
			busService.sendMsg(userBusMessage);
			
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		return true;
	}

	
//	public boolean updateUserBusService(UserDTO user,String busName) {
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			String jsonString = mapper.writeValueAsString(user);
//			busService.sendMsg("updateUser/"+jsonString,busName);
//			
//		} catch (Exception e) {
//            e.printStackTrace();
//        }
//		
//		return true;
//	}

	
	public boolean addUserBusService(UserDTO user) {
		try {
			UserBusMessage userBusMessage = new UserBusMessage(user,"addUser");
			//String jsonString = mapper.writeValueAsString(user);
			busService.sendMsg(userBusMessage);
			
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		return true;
	}

	
//	public boolean addUserBusService(UserDTO user,String busName) {
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			String jsonString = mapper.writeValueAsString(user);
//			busService.sendMsg("addUser/"+jsonString,busName);
//			
//		} catch (Exception e) {
//            e.printStackTrace();
//        }
//		
//		return true;
//	}
	

	public Optional<UserModel> getUser(String id) {
		// TODO Auto-generated method stub
		return userService.getUser(id);
	}

	public List<UserModel> getAllUsers() {
		// TODO Auto-generated method stub
		return userService.getAllUsers();
	}
	
	
	
}
