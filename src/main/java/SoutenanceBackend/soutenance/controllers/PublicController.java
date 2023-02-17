package SoutenanceBackend.soutenance.controllers;


import SoutenanceBackend.soutenance.DtoMapper.RendezVousMapper;
import SoutenanceBackend.soutenance.DtoViewModels.Request.MedecinAvailabilityRequest;
import SoutenanceBackend.soutenance.DtoViewModels.Request.NewAppointmentRequest;
import SoutenanceBackend.soutenance.DtoViewModels.Responses.ApiResponse;
import SoutenanceBackend.soutenance.Models.FuseauHoraire;
import SoutenanceBackend.soutenance.Models.Notification;
import SoutenanceBackend.soutenance.Models.Patient;
import SoutenanceBackend.soutenance.Models.Professionnel;
import SoutenanceBackend.soutenance.Repository.*;
import SoutenanceBackend.soutenance.services.PublicService;
import SoutenanceBackend.soutenance.util.TweakResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Date;
import java.util.Optional;

@CrossOrigin(value = {"http://localhost:8100","http://localhost:4200"},maxAge = 3600,allowCredentials = "true")
@RestController
@RequestMapping("/api/public")

public class PublicController {
@Autowired
FuseauHoraireRepo fuseauHoraireRepository;
    @Autowired
    private PublicService publicService;
    private ProfessionnelRepo professionnelRepo;
    private PatientRepo patientRepository;
    private FuseauHoraireRepo fuseauHoraireRepo;
    private RendezVousRepository rendezVousRepository;

    private RendezVousMapper rendezVousMapper;
    private TweakResponse tweakResponse;
    private final NotificationRepository notificationRepository;

    @Autowired
    public PublicController(ProfessionnelRepo professionnelRepo, PatientRepo patientRepository, FuseauHoraireRepo fuseauHoraireRepo, RendezVousRepository rendezVousRepository, RendezVousMapper rendezVousMapper, TweakResponse tweakResponse,
                            NotificationRepository notificationRepository) {
        this.professionnelRepo = professionnelRepo;
        this.patientRepository = patientRepository;
        this.fuseauHoraireRepo = fuseauHoraireRepo;
        this.rendezVousRepository = rendezVousRepository;

        this.rendezVousMapper = rendezVousMapper;
        this.tweakResponse = tweakResponse;
        this.notificationRepository = notificationRepository;
    }

    //  Add new appointement;
    @PostMapping("rendezvous/save/{idpatient}/{idobjet}")
    public ResponseEntity<?> save(@Valid @RequestBody NewAppointmentRequest newAppointment, BindingResult bindingResult, @PathVariable("idpatient") Long idpatient,@PathVariable ("idobjet")Long idobjet){
        if(bindingResult.hasErrors()){
            throw new ValidationException("Rendez-vous a des erreurs; Le rendez-vous ne peut être sauvegarder;");
        }
        Patient patient = patientRepository.findById(idpatient).get();
        Professionnel professionnel = professionnelRepo.findById(newAppointment.getMedecinId()).get();

        Optional<FuseauHoraire> fr = fuseauHoraireRepository.findById(newAppointment.getShiftTimeId());
        var saveAppointement = publicService.save(newAppointment, idpatient,idobjet);

        Notification notification=new Notification();
        notification.setDate(new Date());
        notification.setMessage("Vous avez un rendez-vous le "+newAppointment.getDate()+ " à " +fr.get().getTimeStart());
        notification.setTitre("RendezVous");
        notification.setIsread(false);
        Notification notification1= notificationRepository.save(notification);
        patient.getNotifications().add(notification1);
        patientRepository.save(patient);
        professionnel.getNotifications().add(notification1);
        professionnelRepo.save(professionnel);


        return ResponseEntity.ok(saveAppointement);

    }

    // EndPoint : List all available Medecin
    @GetMapping("/medecin")
    public ResponseEntity<? extends Object> listMedecin(){
        var ListMedecin = this.publicService.listAllMedecin();
        return ResponseEntity.ok(ListMedecin);
    }

    // End Point : Get list of availability of a doctor by day.
    @PostMapping("/availability")
    public ResponseEntity<? extends Object> listMedecinAvailability(@Valid @RequestBody MedecinAvailabilityRequest medecinAvailability, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ValidationException("Erreur avec disponobilité; Ne peut pas trouver un professionneml disponoible;");
        }
        var result = this.publicService.listAllMedecinAvailability(medecinAvailability);

        if(result==null)
            return new ResponseEntity<>(new ApiResponse(false,"Medecin non fourni.."),
                    HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(result);
    }

}
