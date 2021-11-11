package io.javaminds.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.javaminds.application.entities.Recipient;

public interface RecipientRepository extends JpaRepository<Recipient, Long>{
	
	List<Recipient> findAll();
	
	Recipient findByName(String recipientName);
	
	void deleteByName(String recipientName);

}
