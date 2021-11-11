package io.javaminds.application.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.javaminds.application.entities.PrimaryTransaction;
import io.javaminds.application.entities.User;
import io.javaminds.application.services.TransactionService;
import io.javaminds.application.services.UserService;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class UserResource {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TransactionService transactionService;
	
	@RequestMapping(value = "/user/all", method = RequestMethod.GET)
	public List<User> userList(){
		return userService.findUserList();
	}
	
	@RequestMapping(value = "/user/primaryTransaction", method = RequestMethod.GET)
	public List<PrimaryTransaction> getPrimaryTransactionList(@RequestParam("username") String username){
		return transactionService.findPrimaryTransactionList(username);
	}

}
