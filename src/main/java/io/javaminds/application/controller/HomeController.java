package io.javaminds.application.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashSet;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.javaminds.application.entities.PrimaryAccount;
import io.javaminds.application.entities.Role;
import io.javaminds.application.entities.SavingsAccount;
import io.javaminds.application.entities.User;
import io.javaminds.application.entities.UserRole;
import io.javaminds.application.repositories.RoleRepository;
import io.javaminds.application.services.UserService;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@RequestMapping("/")
	public String home() {

		return "redirect:/index";
	}

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping("/signup")
	public String signup(Model model) {

		User user = new User();
		model.addAttribute("user", user);

		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signupPost(@ModelAttribute("user") User user, Model model) {

		if (userService.checkUserExists(user.getUsername(), user.getEmail())) {

			if (userService.checkEmailExists(user.getEmail())) {
				model.addAttribute("emailExists", true);
			}

			if (userService.checkUsernameExists(user.getUsername())) {
				model.addAttribute("usernameExists", true);
			}

			return "signup";
		} else {

			Set<UserRole> userRoles = new HashSet<>();
			userRoles.add(new UserRole(user, roleRepo.findByName("ROLE_USER")));

			this.userService.createUser(user, userRoles);

			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/signup/validateUserName" , method = RequestMethod.GET)
	@ResponseBody
	public String ajaxCheckingOfUserName(HttpServletRequest req, Model model) {
		
	String username = req.getParameter("username");
		
		return userService.ajaxCheckingOfUserName(username);
		
	}
	
	
	@RequestMapping("/welcomePage")
	public String welcome(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        PrimaryAccount primaryAccount = user.getPrimaryAccount();
        SavingsAccount savingsAccount = user.getSavingsAccount();

        model.addAttribute("primaryAccount", primaryAccount);
        model.addAttribute("savingsAccount", savingsAccount);

        return "welcomePage";
    }

}
