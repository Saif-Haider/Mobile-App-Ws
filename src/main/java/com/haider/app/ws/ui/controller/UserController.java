package com.haider.app.ws.ui.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.haider.app.ws.exceptions.UserServiceException;
import com.haider.app.ws.service.AddressService;
import com.haider.app.ws.service.UserService;
import com.haider.app.ws.shared.dto.AddressDto;
import com.haider.app.ws.shared.dto.UserDto;
import com.haider.app.ws.ui.model.request.UserDetailsRequestModel;
import com.haider.app.ws.ui.model.response.AddressesRest;
import com.haider.app.ws.ui.model.response.ErrorMessages;
import com.haider.app.ws.ui.model.response.OperationStatusModel;
import com.haider.app.ws.ui.model.response.RequestOperationName;
import com.haider.app.ws.ui.model.response.RequestOperationStatus;
import com.haider.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	AddressService addressesService;

	// Path variable {id} is read so as to get the user

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest getUser(@PathVariable String id) {

		UserDto userDto = userService.getUserByUserId(id);
		ModelMapper modelMapper = new ModelMapper();
		UserRest returnValue = modelMapper.map(userDto, UserRest.class);
		return returnValue;
	}

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		UserRest returnValue = new UserRest();
		// UserDto userDto = new UserDto();

		if (userDetails.getFirstName().isEmpty()) {
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		}

		// BeanUtils.copyProperties(userDetails, userDto);
		// ModelMapper like BeanUtils but better
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		UserDto createdUser = userService.createUser(userDto);

		returnValue = modelMapper.map(createdUser, UserRest.class);

		return returnValue;
	}

	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		UserDto updateUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updateUser, returnValue);

		return returnValue;
	}

	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		userService.deleteUser(id);
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

		return returnValue;
	}

	// Will return a list of user with given Page and limit in Request Params
	// Page 0 means first page each page will contain 25 record if we specify page
	// 1 record will
	// Start with 26
	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		List<UserRest> returnValue = new ArrayList<>();
		List<UserDto> users = userService.getUsers(page, limit);

		// Code to copy userRest from userDto
		for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}

		return returnValue;

	}

	 // Return address of a user of particular id
	// localhost:8080/mobile-app-ws/users/{id}/addresses
   // need resources instead of list to use "application/hal+json"
	
	
	@GetMapping(path = "/{id}/addresses", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE ,"application/hal+json"})
	public Resources<AddressesRest> getUserAddresses(@PathVariable String id) {
		List<AddressesRest> returnValue = new ArrayList<>();
		// System.out.println(id);
		List<AddressDto> addressesDto = addressesService.getAddresses(id);

		ModelMapper modelMapper = new ModelMapper();
		// To map list at once uses java.lang.reflect.Type;
		// see in modelmapper user manual generics
		if (addressesDto != null && !addressesDto.isEmpty()) {
			Type listType = new TypeToken<List<AddressesRest>>() {
			}.getType();
			returnValue = modelMapper.map(addressesDto, listType);

			for (AddressesRest addressesRest : returnValue) {
				Link addressLink = linkTo(
						methodOn(UserController.class).getUserAddress(id, addressesRest.getAddressId())).withSelfRel();
				addressesRest.add(addressLink);

				Link userLink = linkTo(methodOn(UserController.class).getUser(id)).withRel("user");
				addressesRest.add(userLink);

			}
		}
		return new Resources<>(returnValue);
	}

	// Get detail of particular address
	@GetMapping(path = "/{id}/addresses/{addressId}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE ,"application/hal+json"})
	public Resource<AddressesRest> getUserAddress(@PathVariable String id, @PathVariable String addressId) {

		AddressDto addressesDto = addressesService.getAddress(addressId);

		ModelMapper modelMapper = new ModelMapper();
		AddressesRest returnValue = modelMapper.map(addressesDto, AddressesRest.class);

		// HateOs means adding link to addressesLink
		// as this linoik refer itself
		// linkTo(UserController.class) tells the path till class

		// HardCoded
//		Link addressLink = linkTo(UserController.class).slash(id).slash("addresses").slash(addressId).withSelfRel();
//		Link userLink = linkTo(UserController.class).slash(id).withRel("user");
//		Link addressesLink = linkTo(UserController.class).slash(id).slash("addresses").withRel("addresses");

		// with use of method On
		Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(id, addressId)).withSelfRel();
		Link userLink = linkTo(methodOn(UserController.class).getUser(id)).withRel("user");
		Link addressesLink = linkTo(methodOn(UserController.class).getUser(id)).slash("addresses").withRel("addresses");

		returnValue.add(addressLink);
		returnValue.add(userLink);
		returnValue.add(addressesLink);

		return new Resource<>(returnValue);
	}

}
