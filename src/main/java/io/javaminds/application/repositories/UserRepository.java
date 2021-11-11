package io.javaminds.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.javaminds.application.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	@Query("select u from User u where u.username =:username")
	public User getUserByUsername(@Param("username") String username);
	
	User findByUsername(String username);
	
    User findByEmail(String email);
    
    List<User> findAll();

}
