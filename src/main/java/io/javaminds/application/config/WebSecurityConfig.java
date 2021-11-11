package io.javaminds.application.config;

import java.security.SecureRandom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService getUserDetailsService() {
		return new CustomUserDetailsServiceImpl();
	}

//	private static final String SALT = "salt"; // Salt should be protected carefully

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		/* return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes())); */
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
							.antMatchers("/admin/**").hasRole("ADMIN")
							.antMatchers("/user/**").hasRole("USER")
							.antMatchers("/***").permitAll()
							.and().formLogin()
							.loginPage("/index")
							.loginProcessingUrl("/index")
							.defaultSuccessUrl("/welcomePage")
							.failureUrl("/index?error")
							.and()
							.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
							.logoutSuccessUrl("/index?logout")
							.deleteCookies("remember-me").permitAll()
			                .and()
			                .rememberMe()
			                .and().csrf().disable();
	}

}