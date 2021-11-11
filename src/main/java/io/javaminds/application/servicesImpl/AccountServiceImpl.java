package io.javaminds.application.servicesImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import io.javaminds.application.entities.PrimaryAccount;
import io.javaminds.application.entities.PrimaryTransaction;
import io.javaminds.application.entities.SavingsAccount;
import io.javaminds.application.entities.SavingsTransaction;
import io.javaminds.application.entities.User;
import io.javaminds.application.repositories.PrimaryAccountRepository;
import io.javaminds.application.repositories.SavingAccountRepository;
import io.javaminds.application.services.AccountService;
import io.javaminds.application.services.TransactionService;
import io.javaminds.application.services.UserService;


@Service
public class AccountServiceImpl implements AccountService{
	
	private static int nextAccountNumber = 995874325;
	
	@Autowired
	private PrimaryAccountRepository priAcctRepo;
	
	@Autowired
	private SavingAccountRepository savAcctRepo;
	
	@Autowired
    private UserService userService;
    
    @Autowired
    private TransactionService transactionService;
	

	@Override
	public PrimaryAccount createPrimaryAccount() {
		PrimaryAccount primaryAccount = new PrimaryAccount();
		primaryAccount.setAccountNumber(accountNumberGen());
		primaryAccount.setAccountBalance(new BigDecimal(0.0));
		
		priAcctRepo.save(primaryAccount);
		return priAcctRepo.findByAccountNumber(primaryAccount.getAccountNumber()) ;
	}

	@Override
	public SavingsAccount createSavingsAccount() {
		SavingsAccount savingAccount = new SavingsAccount();
		savingAccount.setAccountNumber(accountNumberGen());
		savingAccount.setAccountBalance(new BigDecimal(0.0));
		
		savAcctRepo.save(savingAccount);
		return savAcctRepo.findByAccountNumber(savingAccount.getAccountNumber());
	}

	@Override
	public void deposit(String accountType, double amount, Principal principal) {
		User user = userService.findByUsername(principal.getName());

        if (accountType.equalsIgnoreCase("Primary")) {
            PrimaryAccount primaryAccount = user.getPrimaryAccount();
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
            priAcctRepo.save(primaryAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Deposit to Primary Account", "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
            transactionService.savePrimaryDepositTransaction(primaryTransaction);
            
        } else if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            savAcctRepo.save(savingsAccount);

            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Deposit to savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsDepositTransaction(savingsTransaction);
        }
    }

	@Override
	public void withdraw(String accountType, double amount, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		
		if(accountType.equalsIgnoreCase("Primary")) {
			PrimaryAccount primaryAccount = user.getPrimaryAccount();
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			priAcctRepo.save(primaryAccount);
			
			Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Withdraw from Primary Account", "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
            transactionService.savePrimaryDepositTransaction(primaryTransaction);
            
		}else if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savAcctRepo.save(savingsAccount);

            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Withdraw from savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsDepositTransaction(savingsTransaction);
        }
		
	}
	
	private int accountNumberGen() {
        return ++nextAccountNumber;
    }
	
	
}
