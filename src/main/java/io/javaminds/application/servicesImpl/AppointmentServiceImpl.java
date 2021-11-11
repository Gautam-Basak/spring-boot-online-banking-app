package io.javaminds.application.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.javaminds.application.entities.Appointment;
import io.javaminds.application.repositories.AppointmentRepository;
import io.javaminds.application.services.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {
	
	@Autowired
	private AppointmentRepository appointmentRepo;

	@Override
	public Appointment createAppointment(Appointment appointment) {
		
		return appointmentRepo.save(appointment);
	}

	@Override
	public List<Appointment> findAll() {
		
		return appointmentRepo.findAll();
	}

	@Override
	public Appointment findAppointment(Long id) {
		
		return null;
	}

	@Override
	public void confirmAppointment(Long id) {
		
		Appointment appointment = findAppointment(id);
		appointment.setConfirmed(true);
		appointmentRepo.save(appointment);
	}

}
