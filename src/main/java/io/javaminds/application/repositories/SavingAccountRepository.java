package io.javaminds.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import io.javaminds.application.entities.SavingsAccount;

public interface SavingAccountRepository extends JpaRepository<SavingsAccount, Long> {
	
	SavingsAccount findByAccountNumber(int accountNumber);

}
