package io.javaminds.application.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import io.javaminds.application.entities.User;
import io.javaminds.application.repositories.UserRepository;

public class CustomUserDetailsServiceImpl implements UserDetailsService {

	/** The application logger */
	private static final Logger LOG = LoggerFactory.getLogger(CustomUserDetailsServiceImpl.class);

	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = this.userRepo.getUserByUsername(username);

		if (user == null) {
			LOG.warn("Username {} not found", username);
			throw new UsernameNotFoundException("Username " + username + " not found");
		}

		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		return customUserDetails;
	}

}
