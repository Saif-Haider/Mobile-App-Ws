package com.haider.app.ws.service;

import com.haider.app.ws.shared.dto.UserDto;

public interface UserService {
  UserDto createUser(UserDto user);
}
