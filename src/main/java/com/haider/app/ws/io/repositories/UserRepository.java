package com.haider.app.ws.io.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.haider.app.ws.io.entity.UserEntity;


//   PagingAndSortingRepository > CrudRepository > JPA 
@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
     UserEntity findByEmail(String email);

	UserEntity findByUserId(String id);
      
}
