package io.javaminds.application.servicesImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.javaminds.application.entities.PrimaryAccount;
import io.javaminds.application.entities.PrimaryTransaction;
import io.javaminds.application.entities.Recipient;
import io.javaminds.application.entities.SavingsAccount;
import io.javaminds.application.entities.SavingsTransaction;
import io.javaminds.application.entities.User;
import io.javaminds.application.repositories.PrimaryAccountRepository;
import io.javaminds.application.repositories.PrimaryTransactionRepository;
import io.javaminds.application.repositories.RecipientRepository;
import io.javaminds.application.repositories.SavingAccountRepository;
import io.javaminds.application.repositories.SavingTransactionRepository;
import io.javaminds.application.services.TransactionService;
import io.javaminds.application.services.UserService;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private UserService userService;

	@Autowired
	private PrimaryAccountRepository primaryAccountRepo;

	@Autowired
	private SavingAccountRepository savingAccountRepo;

	@Autowired
	private PrimaryTransactionRepository primaryTransactionRepo;

	@Autowired
	private SavingTransactionRepository savingTransactionRepo;

	@Autowired
	private RecipientRepository recipientRepo;

	@Override
	public List<PrimaryTransaction> findPrimaryTransactionList(String username) {
		User user = userService.findByUsername(username);
		List<PrimaryTransaction> primaryTransactionsList = user.getPrimaryAccount().getPrimaryTransactionList();
		return primaryTransactionsList;
	}

	@Override
	public List<SavingsTransaction> findSavingsTransactionList(String username) {
		User user = userService.findByUsername(username);
		List<SavingsTransaction> savingTransactionsList = user.getSavingsAccount().getSavingsTransactionList();
		return savingTransactionsList;
	}

	@Override
	public void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction) {
		primaryTransactionRepo.save(primaryTransaction);

	}

	@Override
	public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction) {
		savingTransactionRepo.save(savingsTransaction);

	}

	@Override
	public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction) {
		primaryTransactionRepo.save(primaryTransaction);

	}

	@Override
	public void saveSavingsWithdrawTransaction(SavingsTransaction savingssTransaction) {
		savingTransactionRepo.save(savingssTransaction);

	}

	@Override
	public void betweenAccountsTransfer(String transferFrom, String transferTo, String amount,
			PrimaryAccount primaryAccount, SavingsAccount savingsAccount) throws Exception {
		if (transferFrom.equalsIgnoreCase("Primary") && transferTo.equalsIgnoreCase("Savings")) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
			primaryAccountRepo.save(primaryAccount);
			savingAccountRepo.save(savingsAccount);

			Date date = new Date();

			PrimaryTransaction primaryTransaction = new PrimaryTransaction(date,
					"Transfer from " + transferFrom + " to " + transferTo + " Account", "Account Transfer", "Finished",
					Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
			primaryTransactionRepo.save(primaryTransaction);

			SavingsTransaction savingTransaction = new SavingsTransaction(date,
					"Received from " + transferFrom + " Account ", "Account Transfer", "Finished",
					Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
			savingTransactionRepo.save(savingTransaction);

		} else if (transferFrom.equalsIgnoreCase("Savings") && transferTo.equalsIgnoreCase("Primary")) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));

			primaryAccountRepo.save(primaryAccount);
			savingAccountRepo.save(savingsAccount);

			Date date = new Date();

			SavingsTransaction savingTransaction = new SavingsTransaction(date,
					"Transfer from " + transferFrom + " to " + transferTo + " Account", "Account Transfer", "Finished",
					Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
			savingTransactionRepo.save(savingTransaction);

			PrimaryTransaction primaryTransaction = new PrimaryTransaction(date,
					"Received from " + transferFrom + " Account ", "Account Transfer", "Finished",
					Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
			primaryTransactionRepo.save(primaryTransaction);
		} else {
			throw new Exception("Invalid Transaction");
		}
	}

	@Override
	public List<Recipient> findRecipientList(Principal principal) {

		String username = principal.getName();
		List<Recipient> recipientList = recipientRepo.findAll().stream()
				.filter(recipient -> username.equals(recipient.getUser().getUsername())).collect(Collectors.toList());

		return recipientList;
	}

	@Override
	public Recipient saveRecipient(Recipient recipient) {

		return recipientRepo.save(recipient);
	}

	@Override
	public Recipient findRecipientByName(String recipientName) {

		return recipientRepo.findByName(recipientName);
	}

	@Override
	public void deleteRecipientByname(String recipientName) {
		recipientRepo.deleteByName(recipientName);

	}

	@Override
	public void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount,
			PrimaryAccount primaryAccount, SavingsAccount savingsAccount) {

		if (accountType.equalsIgnoreCase("Primary")) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccountRepo.save(primaryAccount);

			Date date = new Date();

			PrimaryTransaction primaryTransaction = new PrimaryTransaction(date,
					"Transfer To Recipient " + recipient.getName(), "Account", "Finished", Double.parseDouble(amount),
					primaryAccount.getAccountBalance(), primaryAccount);
			primaryTransactionRepo.save(primaryTransaction);
		} else if (accountType.equalsIgnoreCase("Savings")) {
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingAccountRepo.save(savingsAccount);

			Date date = new Date();

			SavingsTransaction savingsTransaction = new SavingsTransaction(date,
					"Transfer To Recipient " + recipient.getName(), "Account", "Finished", Double.parseDouble(amount),
					savingsAccount.getAccountBalance(), savingsAccount);
			savingTransactionRepo.save(savingsTransaction);
		}

	}

}