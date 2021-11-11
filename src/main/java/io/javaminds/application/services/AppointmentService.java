package io.javaminds.application.services;

import java.util.List;
import io.javaminds.application.entities.Appointment;


public interface AppointmentService {
	Appointment createAppointment(Appointment appointment);

    List<Appointment> findAll();

    Appointment findAppointment(Long id);

    void confirmAppointment(Long id);
}
