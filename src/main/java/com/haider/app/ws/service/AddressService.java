package com.haider.app.ws.service;

import java.util.List;

import com.haider.app.ws.shared.dto.AddressDto;;

public interface AddressService {
	List<AddressDto> getAddress(String userId);
}
