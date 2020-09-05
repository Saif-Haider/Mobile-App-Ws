package com.haider.app.ws.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haider.app.ws.UserRepository;
import com.haider.app.ws.io.entity.UserEntity;
import com.haider.app.ws.service.UserService;
import com.haider.app.ws.shared.Utils;
import com.haider.app.ws.shared.dto.UserDto;



@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
    
	@Autowired
	Utils utils;
	
	@Override
	public UserDto createUser(UserDto user) {

		if (userRepository.findByEmail(user.getEmail()) != null)   // IF RECORD ALREADY EXIST THEN THROW EXCEPTION
			throw new RuntimeException("Record already exists");
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
        
		String publicUserId = utils.generateUserId(30);
		userEntity.setEncryptedPassword("test");
		userEntity.setUserId(publicUserId);

		UserEntity storedUserDetails = userRepository.save(userEntity);

		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, returnValue);

		return returnValue;
	}

}
