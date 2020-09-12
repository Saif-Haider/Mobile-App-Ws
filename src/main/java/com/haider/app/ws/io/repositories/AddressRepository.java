package com.haider.app.ws.io.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.haider.app.ws.io.entity.AddressEntity;
import com.haider.app.ws.io.entity.UserEntity;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {

	Iterable<AddressEntity> findAllByUserDetails(UserEntity userEntity);

	

}
