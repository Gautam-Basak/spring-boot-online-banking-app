package io.javaminds.application.services;

import java.util.List;
import java.util.Set;
import io.javaminds.application.entities.User;
import io.javaminds.application.entities.UserRole;

public interface UserService {

	User findByUsername(String username);
	
	public String ajaxCheckingOfUserName(String username);

	User findByEmail(String email);

	boolean checkUserExists(String username, String email);

	boolean checkUsernameExists(String username);

	boolean checkEmailExists(String email);

	void save(User user);

	User createUser(User user, Set<UserRole> userRoles);

	User saveUser(User user);

	List<User> findUserList();

	void enableUser(String username);

	void disableUser(String username);

}
