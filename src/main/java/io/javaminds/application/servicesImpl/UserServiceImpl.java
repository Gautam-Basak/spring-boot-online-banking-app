package io.javaminds.application.servicesImpl;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.javaminds.application.entities.Role;
import io.javaminds.application.entities.User;
import io.javaminds.application.entities.UserRole;
import io.javaminds.application.repositories.RoleRepository;
import io.javaminds.application.repositories.UserRepository;
import io.javaminds.application.services.AccountService;
import io.javaminds.application.services.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private AccountService accountService;

	@Override
	public User findByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Override
	public boolean checkUserExists(String username, String email) {
		if (checkUsernameExists(username) || checkEmailExists(email)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean checkUsernameExists(String username) {
		if (null != findByUsername(username)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean checkEmailExists(String email) {
		if (null != findByEmail(email)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void save(User user) {
		userRepo.save(user);

	}

	@Override
	public User createUser(User user, Set<UserRole> userRoles) {

		User localUser = userRepo.findByUsername(user.getUsername());

		if (localUser != null) {
			LOG.info("User with username {} already exist. Nothing will be done. ", user.getUsername());
		} else {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			for (UserRole ur : userRoles) {
				roleRepo.save(ur.getRole());
			}
			user.getUserRoles().addAll(userRoles);

			user.setPrimaryAccount(accountService.createPrimaryAccount());
			user.setSavingsAccount(accountService.createSavingsAccount());

			localUser = userRepo.save(user);
		}
		return localUser;
	}

	@Override
	public User saveUser(User user) {
		return userRepo.save(user);
	}

	@Override
	public List<User> findUserList() {
		return userRepo.findAll();
	}

	@Override
	public void enableUser(String username) {
		User user = findByUsername(username);
		user.setEnabled(true);
		userRepo.save(user);

	}

	@Override
	public void disableUser(String username) {
		User user = findByUsername(username);
		user.setEnabled(false);
		System.out.println(user.isEnabled());
		userRepo.save(user);
		System.out.println(username + " is disabled.");

	}

	@Override
	public String ajaxCheckingOfUserName(String username) {
		User user = userRepo.getUserByUsername(username);
		return (user == null) ? "Unique" : "Duplicate";
	}

}
