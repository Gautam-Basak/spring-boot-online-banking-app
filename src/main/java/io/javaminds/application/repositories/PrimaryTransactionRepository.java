package io.javaminds.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.javaminds.application.entities.PrimaryTransaction;

public interface PrimaryTransactionRepository extends JpaRepository<PrimaryTransaction, Long> {
	
	List<PrimaryTransaction> findAll();

}
