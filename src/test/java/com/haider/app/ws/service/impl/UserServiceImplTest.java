package com.haider.app.ws.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.haider.app.ws.io.entity.UserEntity;
import com.haider.app.ws.io.repositories.UserRepository;
import com.haider.app.ws.shared.dto.UserDto;

class UserServiceImplTest {

	// this will inject the mock objects when creating this class as without this
	// the
	// mock object will not be inserted in userServiceImpl
	@InjectMocks
	UserServiceImpl userService;

	// We need to Mock as UserService will be null as it is out of scope of the
	// function being tested
	//
	@Mock
	UserRepository userRepository;

	@BeforeEach
	void setUp() throws Exception {
		// For mockito to initiate this mocks

		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetUser() {
		UserEntity userEntity = new UserEntity();

		// Dummy Entity initialized

		userEntity.setId(1L);
		userEntity.setFirstName("Saif");
		userEntity.setEncryptedPassword("msahuiewygr7834u");
		userEntity.setUserId("sdferdst");

		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		
		// this will work as above line as above line conforms above statement make 
		// userRepositorty return a entity when any string is provided
		UserDto userDto = userService.getUser("iammail@mail.com");
		
		assertNotNull(userDto);
		assertEquals("Saif", userDto.getFirstName());
	}

}
