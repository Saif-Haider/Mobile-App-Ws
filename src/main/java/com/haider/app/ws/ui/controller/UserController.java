package com.haider.app.ws.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haider.app.ws.exceptions.UserServiceException;
import com.haider.app.ws.service.UserService;
import com.haider.app.ws.shared.dto.UserDto;
import com.haider.app.ws.ui.model.request.UserDetailsRequestModel;
import com.haider.app.ws.ui.model.response.ErrorMessages;
import com.haider.app.ws.ui.model.response.OperationStatusModel;
import com.haider.app.ws.ui.model.response.RequestOperationName;
import com.haider.app.ws.ui.model.response.RequestOperationStatus;
import com.haider.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	UserService userService;

	// Path variable {id} is read so as to get the user

	@GetMapping(path = "/{id}",
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest getUser(@PathVariable String id) {
		UserRest returnValue = new UserRest();

		UserDto userDto = userService.getUserByUserId(id);
		BeanUtils.copyProperties(userDto, returnValue);
		return returnValue;
	}

	@PostMapping(
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		UserRest returnValue = new UserRest();
		UserDto userDto = new UserDto();

		if (userDetails.getFirstName().isEmpty()) {
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		}
		BeanUtils.copyProperties(userDetails, userDto);
		UserDto createdUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createdUser, returnValue);

		return returnValue;
	}

	
	
	@PutMapping(path = "/{id}",
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, 
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public UserRest updateUser(@PathVariable String id,@RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();
		UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);
		UserDto updateUser = userService.updateUser(id,userDto);
		BeanUtils.copyProperties(updateUser, returnValue);

		return returnValue;
	}

	@DeleteMapping(path = "/{id}",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		userService.deleteUser(id);
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		
		return returnValue;
	}

}
