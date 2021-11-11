package io.javaminds.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import io.javaminds.application.entities.PrimaryAccount;

public interface PrimaryAccountRepository extends JpaRepository<PrimaryAccount, Long>{
	
	PrimaryAccount findByAccountNumber(int accountNumber);

}
