package SoutenanceBackend.soutenance.services;

import SoutenanceBackend.soutenance.DtoViewModels.Request.TodayAppointmentRequest;
import SoutenanceBackend.soutenance.Models.Patient;
import SoutenanceBackend.soutenance.Models.Professionnel;
import SoutenanceBackend.soutenance.Models.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.List;

public interface ProfesionnelService {

    List<Professionnel> lister(Professionnel professionnel);
    Object Modifier(Long id, Professionnel professionnel);
    Object deleteEvent(String rendezVousId);

    Object changeEventStatus(String appointmentId);

    Object AllMedecinAvailability(UserDetailsImpl currentUser, TodayAppointmentRequest todayAppointment);
}
