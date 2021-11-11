package io.javaminds.application.services;

import java.security.Principal;

import io.javaminds.application.entities.PrimaryAccount;
import io.javaminds.application.entities.SavingsAccount;


public interface AccountService {
	
	PrimaryAccount createPrimaryAccount();
    SavingsAccount createSavingsAccount();
    void deposit(String accountType, double amount, Principal principal);
    void withdraw(String accountType, double amount, Principal principal);

}
