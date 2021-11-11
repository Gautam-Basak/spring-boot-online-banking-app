package io.javaminds.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.javaminds.application.entities.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
	
	List<Appointment> findAll();

}
