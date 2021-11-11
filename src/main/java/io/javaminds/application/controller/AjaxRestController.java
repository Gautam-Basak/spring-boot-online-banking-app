package io.javaminds.application.controller;

import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.javaminds.application.entities.User;
import io.javaminds.application.services.UserService;

@RestController
public class AjaxRestController {

	@Autowired
    private UserService userService;
	
	@RequestMapping(value = "account/deposit/getAmount" , method = RequestMethod.GET)
	public BigDecimal accountBalance(Principal principal) {
		
		
		User user = userService.findByUsername(principal.getName());
		BigDecimal data = user.getPrimaryAccount().getAccountBalance();
		
		return data;
		
	}
}
