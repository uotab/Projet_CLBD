package com.cpe.projet.springboot.common.tools;

import com.cpe.projet.springboot.user.UserDTO;
import com.cpe.projet.springboot.user.UserModel;

public class DTOMapper {
	
	public static UserDTO fromUserModelToUserDTO(UserModel uM) {
		UserDTO uDto =new UserDTO(uM);
		return uDto;
	}
	
}
