package com.cpe.projet.springboot.user;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cpe.projet.springboot.auth.AuthDTO;
import com.cpe.projet.springboot.common.tools.DTOMapper;





//ONLY FOR TEST NEED ALSO TO ALLOW CROOS ORIGIN ON WEB BROWSER SIDE
@CrossOrigin
@RestController
public class UserRestController {

	

	private final Multiplexer multiplexer;
	

	public UserRestController(Multiplexer multiplexer) {
		this.multiplexer=multiplexer;
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/users")
	private List<UserDTO> getAllUsers() {
		List<UserDTO> uDTOList=new ArrayList<UserDTO>();
		for(UserModel uM: multiplexer.getAllUsers()){
			uDTOList.add(DTOMapper.fromUserModelToUserDTO(uM));
		}
		return uDTOList;

	}
	
	@RequestMapping(method=RequestMethod.GET,value="/user/{id}")
	private UserDTO getUser(@PathVariable String id) {
		Optional<UserModel> ruser;
		ruser= multiplexer.getUser(id);
		if(ruser.isPresent()) {
			return DTOMapper.fromUserModelToUserDTO(ruser.get());
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id:"+id+", not found",null);

	}
	
	@RequestMapping(method=RequestMethod.POST,value="/user")
	public boolean addUser(@RequestBody UserDTO user) {		
		return multiplexer.addUserBusService(user);
	}
	
//	@RequestMapping(method=RequestMethod.POST,value="/user/{busName}")
//	public boolean addUser(@RequestBody UserDTO user,@PathVariable String busName) {
//		return multiplexer.addUserBusService(user,busName);
//	}
	
	
	
	@RequestMapping(method=RequestMethod.PUT,value="/user/{id}")
	public boolean updateUser(@RequestBody UserDTO user,@PathVariable String id) {
		user.setId(Integer.valueOf(id));
		return multiplexer.updateUserBusService(user);
	}
	
	@RequestMapping(method=RequestMethod.DELETE,value="/user/{id}")
	public void deleteUser(@PathVariable String id) {
		multiplexer.deleteUserBusService(id);
	}
	
//	@RequestMapping(method=RequestMethod.PUT,value="/user/{id}/{busName}")
//	public boolean updateUser(@RequestBody UserDTO user,@PathVariable String id,@PathVariable String busName) {
//		user.setId(Integer.valueOf(id));
//		return multiplexer.updateUserBusService(user,busName);
//	}
//	
//	@RequestMapping(method=RequestMethod.DELETE,value="/user/{id}/{busName}")
//	public void deleteUser(@PathVariable String id,@PathVariable String busName) {
//		multiplexer.deleteUserBusService(id,busName);
//	}
	
	@RequestMapping(method=RequestMethod.POST,value="/auth")
	private Integer getAllCourses(@RequestBody AuthDTO authDto) {
		List<UserModel> uList = multiplexer.getUserByLoginPwd(authDto.getUsername(),authDto.getPassword());
		if( uList.size() > 0) {
			return uList.get(0).getId();
		}
		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Authentification Failed",null);
	}
}
