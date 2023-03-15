package com.cpe.projet.springboot.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;

import com.cpe.projet.springboot.common.tools.DTOMapper;



@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<UserModel> getAllUsers() {
		List<UserModel> userList = new ArrayList<>();
		userRepository.findAll().forEach(userList::add);
		return userList;
	}

	public Optional<UserModel> getUser(String id) {
		return userRepository.findById(Integer.valueOf(id));
	}

	public Optional<UserModel> getUser(Integer id) {
		return userRepository.findById(id);
	}
	public boolean ReceiveMessageFromBus(UserBusMessage msg) {
		// TO DO
		String functionName = msg.getFunctionName();
		if(functionName.equals("addUser")) {
			try {
				UserDTO user = msg.getUser();
				this.addUser(user);
			} catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		if(functionName.equals("deleteUser")) {
			try {
				String id = msg.getId();
				this.deleteUser(id);
			} catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		if(functionName.equals("updateUser")) {
			try {
				UserDTO user = msg.getUser();
				this.updateUser(user);
			} catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		
		return true;
	}
	
	
	
	public UserDTO addUser(UserDTO user) {
		System.out.println("ajout de user xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		UserModel u = fromUDtoToUModel(user);
		UserModel uBd = userRepository.save(u);
		return DTOMapper.fromUserModelToUserDTO(uBd);
	}

	public UserDTO updateUser(UserDTO user) {
		UserModel u = fromUDtoToUModel(user);
		UserModel uBd =userRepository.save(u);
		return DTOMapper.fromUserModelToUserDTO(uBd);
	}

	public UserDTO updateUser(UserModel user) {
		UserModel uBd = userRepository.save(user);
		return DTOMapper.fromUserModelToUserDTO(uBd);
	}

	public void deleteUser(String id) {
		userRepository.deleteById(Integer.valueOf(id));
	}

	public List<UserModel> getUserByLoginPwd(String login, String pwd) {
		List<UserModel> ulist = null;
		ulist = userRepository.findByLoginAndPwd(login, pwd);
		return ulist;
	}

	private UserModel fromUDtoToUModel(UserDTO user) {
		UserModel u = new UserModel(user);
		return u;
	}

}
