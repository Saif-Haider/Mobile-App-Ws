package com.haider.app.ws.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.haider.app.ws.exceptions.UserServiceException;
import com.haider.app.ws.io.entity.UserEntity;
import com.haider.app.ws.io.repositories.UserRepository;
import com.haider.app.ws.service.UserService;
import com.haider.app.ws.shared.Utils;
import com.haider.app.ws.shared.dto.AddressDto;
import com.haider.app.ws.shared.dto.UserDto;
import com.haider.app.ws.ui.model.response.ErrorMessages;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto createUser(UserDto user) {

		if (userRepository.findByEmail(user.getEmail()) != null) // IF RECORD ALREADY EXIST THEN THROW EXCEPTION
			throw new RuntimeException("Record already exists");
//		 = new UserEntity();
//		BeanUtils.copyProperties(user, userEntity);
		
		// Loop to set address dto in user dto and userdto in address dto
		// return list return a reference to the list so it can be changed by getAddresses
		for(int i=0;i<user.getAddresses().size();i++) {
			AddressDto addressDto = user.getAddresses().get(i);
			addressDto.setUserDetails(user);
			addressDto.setAddressId(utils.generateAddressId(30));
			user.getAddresses().set(i, addressDto);
		}
		
		
		
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity =  modelMapper.map(user, UserEntity.class);

		String publicUserId = utils.generateUserId(30);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword())); // ENCRYPT PASSWORD
		userEntity.setUserId(publicUserId);

		UserEntity storedUserDetails = userRepository.save(userEntity);

		UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);
	// BeanUtils.copyProperties(storedUserDetails, returnValue);

		return returnValue;
	}

	// RETURN USERNAME & PASSWORD AFTER SEARCHING FROM DATABASE RETURN A USER OBJECT

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	// TO FIND USER BY MAIL AND RETURN IT

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException(email);
		}

		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);

		return returnValue;

	}

	@Override
	public UserDto getUserByUserId(String id) {

		UserEntity userEntity = userRepository.findByUserId(id);
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);

		return returnValue;

	}

	@Override
	public UserDto updateUser(String userId, UserDto userDto) {
		UserDto returnValue = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());

		UserEntity updatedUserDetails = userRepository.save(userEntity);
		BeanUtils.copyProperties(updatedUserDetails, returnValue);
		return returnValue;
	}

	@Override
	public void deleteUser(String userId) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}
		
		userRepository.delete(userEntity);
	}
   // method to return list of users
   // using findAll pagable  it returns a Page
	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<>();
		
		if(page>0) {
			page--;
		}
		
		Pageable pageable = PageRequest.of(page, limit);
		Page<UserEntity> usersPage  = userRepository.findAll(pageable );
		List<UserEntity> users = usersPage.getContent();
		
		
		// Code to copy userDto from UserEntity
				for (UserEntity userEntity : users) {
					UserDto userDto = new UserDto();
					BeanUtils.copyProperties(userEntity, userDto);
					returnValue.add(userDto);
				}
		
		return returnValue;
	}

}
