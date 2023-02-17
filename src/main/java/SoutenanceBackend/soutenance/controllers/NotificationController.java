package SoutenanceBackend.soutenance.controllers;

import SoutenanceBackend.soutenance.Models.Notification;
import SoutenanceBackend.soutenance.Repository.NotificationRepository;
import SoutenanceBackend.soutenance.Repository.PatientRepo;
import SoutenanceBackend.soutenance.Repository.ProfessionnelRepo;
import SoutenanceBackend.soutenance.Repository.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private  RendezVousRepository rendezVousRepository;

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private ProfessionnelRepo professionnelRepo;
    @Autowired
    private NotificationRepository notificationRepository;


/*    @Scheduled(fixedDelay = 60000)
    public void EnvoyerNotificationRdv(){
        List<Professionnel> profList=professionnelRepo.findAll();
        List<Patient>patientList=patientRepo.findAll();

        LocalDate today=LocalDate.now();

        for (Professionnel p:profList){

        }
    }*/


   @GetMapping("/affiher/{id_notification}")
    public Optional<Notification>findNotifByiD(@PathVariable Long id_notification){
      return notificationRepository.findById(id_notification);
   }

}
