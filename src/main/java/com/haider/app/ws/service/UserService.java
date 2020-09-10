package com.haider.app.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.haider.app.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto user);

	UserDto getUser(String user);

	UserDto getUserByUserId(String id);

	UserDto updateUser(String userId, UserDto userDto);

	void deleteUser(String userId);

	List<UserDto> getUsers(int page, int limit);
}
