package io.javaminds.application.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.javaminds.application.entities.PrimaryAccount;
import io.javaminds.application.entities.Recipient;
import io.javaminds.application.entities.SavingsAccount;
import io.javaminds.application.entities.User;
import io.javaminds.application.services.TransactionService;
import io.javaminds.application.services.UserService;

@Controller
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;
    
    
    @RequestMapping(value = "/betweenAccounts", method = RequestMethod.GET)
    public String betweenAccounts(Model model) {
        model.addAttribute("transferFrom", "");
        model.addAttribute("transferTo", "");
        model.addAttribute("amount", "");

        return "betweenAccounts";
    }

    @RequestMapping(value = "/betweenAccounts", method = RequestMethod.POST)
    public String betweenAccountsPost(
            @ModelAttribute("transferFrom") String transferFrom,
            @ModelAttribute("transferTo") String transferTo,
            @ModelAttribute("amount") String amount,
            Principal principal) throws Exception
    {
        User user = userService.findByUsername(principal.getName());
        PrimaryAccount primaryAccount = user.getPrimaryAccount();
        SavingsAccount savingsAccount = user.getSavingsAccount();
        transactionService.betweenAccountsTransfer(transferFrom, transferTo, amount, primaryAccount, savingsAccount);

        return "redirect:/welcomePage";
    }
    
    @RequestMapping(value = "/recipient", method = RequestMethod.GET)
    public String recipient(Model model, Principal principal) {
    	List<Recipient> recipientList = transactionService.findRecipientList(principal);
    	
    	Recipient recipient = new Recipient();

        model.addAttribute("recipientList", recipientList);
        model.addAttribute("recipient", recipient);

        return "recipient";
    }
    
    @RequestMapping(value = "/recipient/save", method = RequestMethod.POST)
    public String addRecipient(@ModelAttribute("recipient") Recipient recipient, Principal principal, Model model) {
    	User user = userService.findByUsername(principal.getName());
    	
    	recipient.setUser(user);
    	transactionService.saveRecipient(recipient);
    	
    	return "redirect:/transfer/recipient";
    	
    }
    
    @RequestMapping(value = "/recipient/edit", method = RequestMethod.GET)
    public String editRecipient(@RequestParam("recipientName") String recipientName, Model model, Principal principal) {
    	
    	Recipient recipient = transactionService.findRecipientByName(recipientName);
    	List<Recipient> recipientList = transactionService.findRecipientList(principal);
    	
    	model.addAttribute("recipient", recipient);
    	model.addAttribute("recipientList", recipientList);
    	
    	return "recipient";
    }
    
    @RequestMapping(value = "/recipient/delete", method = RequestMethod.GET)
    @Transactional
    public String recipientDelete(@RequestParam(value = "recipientName") String recipientName, Model model, Principal principal){

        transactionService.deleteRecipientByname(recipientName);

        List<Recipient> recipientList = transactionService.findRecipientList(principal);

        Recipient recipient = new Recipient();
        model.addAttribute("recipient", recipient);
        model.addAttribute("recipientList", recipientList);


        return "recipient";
    }
    
    @RequestMapping(value = "/toOthers", method = RequestMethod.GET)
    public String toOtherBank(Model model, Principal principal) {
        List<Recipient> recipientList = transactionService.findRecipientList(principal);

        model.addAttribute("recipientList", recipientList);
        model.addAttribute("accountType", "");

        return "toOthers";
    }
    
    @RequestMapping(value = "/toOthers", method = RequestMethod.POST)
    public String toOtherBankPost(
    		@ModelAttribute("recipientName") String recipientName,
    		@ModelAttribute("accountType") String accountType,
    		@ModelAttribute("amount") String amount,
    		Principal principal) {
    	User user = userService.findByUsername(principal.getName());
    	Recipient recipient = transactionService.findRecipientByName(recipientName);
    	transactionService.toSomeoneElseTransfer(recipient, accountType, amount, user.getPrimaryAccount(), user.getSavingsAccount());
    	
    	return "redirect:/welcomePage";
    }
    		
}