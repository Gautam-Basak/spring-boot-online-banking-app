package io.javaminds.application.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.javaminds.application.entities.PrimaryAccount;
import io.javaminds.application.entities.PrimaryTransaction;
import io.javaminds.application.entities.SavingsAccount;
import io.javaminds.application.entities.SavingsTransaction;
import io.javaminds.application.entities.User;
import io.javaminds.application.services.AccountService;
import io.javaminds.application.services.TransactionService;
import io.javaminds.application.services.UserService;


@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private TransactionService transactionService;
	
	@RequestMapping("/primaryAccount")
	public String primaryAccount(Model model, Principal principal) {
		List<PrimaryTransaction> primaryTransactionList = transactionService.findPrimaryTransactionList(principal.getName());
		
		User user = userService.findByUsername(principal.getName());
        PrimaryAccount primaryAccount = user.getPrimaryAccount();

        model.addAttribute("primaryAccount", primaryAccount);
        model.addAttribute("primaryTransactionList", primaryTransactionList);
		
		return "primaryAccount";
	}

	@RequestMapping("/savingsAccount")
    public String savingsAccount(Model model, Principal principal) {
		List<SavingsTransaction> savingsTransactionList = transactionService.findSavingsTransactionList(principal.getName());
        User user = userService.findByUsername(principal.getName());
        SavingsAccount savingsAccount = user.getSavingsAccount();

        model.addAttribute("savingsAccount", savingsAccount);
        model.addAttribute("savingsTransactionList", savingsTransactionList);

        return "savingsAccount";
    }
	
	@RequestMapping(value = "/deposit", method = RequestMethod.GET)
    public String deposit(Model model) {
        model.addAttribute("accountType", "");
        model.addAttribute("amount", "");

        return "deposit";
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    public String depositPOST(@ModelAttribute("amount") String amount, @ModelAttribute("accountType") String accountType, Principal principal) {
        accountService.deposit(accountType, Double.parseDouble(amount), principal);

        return "redirect:/welcomePage";
    }
    
	/*
	 * @RequestMapping(value = "/account/deposit/getBalanceAmount" , method =
	 * RequestMethod.GET)
	 * 
	 * @ResponseBody public BigDecimal accountBalance(Principal principal) { User
	 * user = userService.findByUsername(principal.getName()); BigDecimal data =
	 * user.getPrimaryAccount().getAccountBalance();
	 * 
	 * return data;
	 * 
	 * }
	 */
    
    @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
    public String withdraw(Model model) {
        model.addAttribute("accountType", "");
        model.addAttribute("amount", "");

        return "withdraw";
    }
    
    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public String withdrawPOST(@ModelAttribute("amount") String amount, @ModelAttribute("accountType") String accountType, Principal principal) {
        accountService.withdraw(accountType, Double.parseDouble(amount), principal);

        return "redirect:/welcomePage";
    }
    
    
    //Handling Invalid Transaction like withdraw amount is more than balance amount
    
    /****
    
    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public String withdrawPOST(@ModelAttribute("amount") String amount, @ModelAttribute("accountType") String accountType, Principal principal) throws Exception {
        
		User user = userService.findByUsername(principal.getName());
    	
       BigDecimal balanceAmount = user.getPrimaryAccount().getAccountBalance();
       
       
       
       //Converting String(amount) to Integer
       int a = Integer.parseInt(amount);
       
       //Converting BigDecimal(balanceAmount) to Integer
       int b;
       b=balanceAmount.intValue();
       
       
    	if(a>b) {
    		throw new Exception("Low Account Balance");
    	}else {
    		accountService.withdraw(accountType, Double.parseDouble(amount), principal);
		}
 
        return "redirect:/welcomePage";
    }

	***/

}
