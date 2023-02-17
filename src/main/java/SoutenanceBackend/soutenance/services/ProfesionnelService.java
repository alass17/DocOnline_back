package SoutenanceBackend.soutenance.services;

import SoutenanceBackend.soutenance.DtoViewModels.Request.TodayAppointmentRequest;
import SoutenanceBackend.soutenance.Models.Patient;
import SoutenanceBackend.soutenance.Models.Professionnel;
import SoutenanceBackend.soutenance.Models.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;

public interface ProfesionnelService {

    List<Professionnel> lister(Professionnel professionnel);
    Object Modifier(Long id, Professionnel professionnel);

    Object AllMedecinAvailability(TodayAppointmentRequest todayAppointment,Long id_prof);

    Object deleteEvent(String rendezVousId);

    Object changeEventStatus(String appointmentId);

    Object AllMedecinAvailability(User currentUser, @RequestBody TodayAppointmentRequest todayAppointment);
}
