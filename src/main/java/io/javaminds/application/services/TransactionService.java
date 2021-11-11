package io.javaminds.application.services;

import java.security.Principal;
import java.util.List;

import io.javaminds.application.entities.PrimaryAccount;
import io.javaminds.application.entities.PrimaryTransaction;
import io.javaminds.application.entities.Recipient;
import io.javaminds.application.entities.SavingsAccount;
import io.javaminds.application.entities.SavingsTransaction;

public interface TransactionService {
	
	List<PrimaryTransaction> findPrimaryTransactionList(String username);
	
	List<SavingsTransaction> findSavingsTransactionList(String username);
	
	void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction);
	
	void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction);
	
	void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction);
	
	void saveSavingsWithdrawTransaction(SavingsTransaction savingssTransaction);
	
	void betweenAccountsTransfer(String transferFrom, String transferTo, String amount, PrimaryAccount primaryAccount, SavingsAccount savingsAccount) throws Exception;
	
	List<Recipient> findRecipientList(Principal principal);
	
	Recipient saveRecipient(Recipient recipient);
	
	Recipient findRecipientByName(String recipientName);
	
	void deleteRecipientByname(String recipientName);
	
	void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount, PrimaryAccount primaryAccount, SavingsAccount savingsAccount);
	

}
