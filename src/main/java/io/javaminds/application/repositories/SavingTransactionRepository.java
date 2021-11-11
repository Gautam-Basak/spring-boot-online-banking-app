package io.javaminds.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.javaminds.application.entities.SavingsTransaction;

public interface SavingTransactionRepository extends JpaRepository<SavingsTransaction, Long> {
	
	List<SavingsTransaction> findAll();

}
