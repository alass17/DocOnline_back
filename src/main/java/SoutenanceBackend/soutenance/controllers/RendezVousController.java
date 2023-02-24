package SoutenanceBackend.soutenance.controllers;


import SoutenanceBackend.soutenance.Models.*;
import SoutenanceBackend.soutenance.Repository.*;
import SoutenanceBackend.soutenance.services.PatientService;
import SoutenanceBackend.soutenance.services.RendezVousService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(value = {"http://localhost:8100","http://localhost:4200"},maxAge = 3600,allowCredentials = "true")

@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/rendezvous")
public class RendezVousController {

    @Autowired
    private RendezVousService rendezVousService;

    @Autowired
    private PatientService patientService;

    @Autowired
    PatientRepo patientRepo;
    @Autowired
    ProfessionnelRepo professionnelRepo;

    @Autowired
    ObjetRdvRepository objetRdvRepository;
    @Autowired
    private RendezVousRepository rendezVousRepository;
    @Autowired
    private FuseauHoraireRepo fuseauHoraireRepo;
    @Autowired
    private NotificationRepository notificationRepository;

    /*@PostMapping("/ajouter/{idpatient}/{idprofessionnel}")

    public RendezVous ajouter (@RequestBody RendezVous rendezVous,
                               @PathVariable Long idpatient,
                               @PathVariable Long idprofessionnel){
        Patient patient = patientService.trouverParId(idpatient);
        Professionnel professionnel = professionnelRepo.findById(idprofessionnel).get();
        professionnel.getPatients().add(patient);
        professionnelRepo.save(professionnel);
        return rendezVousService.ajouter(rendezVous);
    }*/

    @PostMapping("/ajouter/{id_professionnel}/{id_patient}")

    public String ajouter (@RequestBody RendezVous rendezVous,
                           @PathVariable Long id_professionnel,
                           @PathVariable Long id_patient)

        {
        try{
            /*RendezVous rendezVous=new RendezVous();
            rendezVous.setJour(jour);
            rendezVous.setHeure(heure);*/
            Professionnel professionnel = professionnelRepo.getReferenceById(id_professionnel);
            rendezVous.setProfessionnel(professionnel);
            Patient  patient = patientRepo.getReferenceById(id_patient);
            rendezVous.setPatient(patient);

        rendezVousService.ajouter(rendezVous);
        return "Ajouté avec succès";
    } catch (Exception e){
            return e.getMessage();
        }
    }


    @GetMapping("/afficher")
    public List<RendezVous> afficher() {
        List<RendezVous> appointments = rendezVousRepository.findAll();
        appointments.size(); // force loading of all appointments
        return appointments;
    }


    @DeleteMapping("/supprimer/{idrdv}")
    public String supprimer(@PathVariable Long idrdv) {
        return rendezVousService.Supprimer(idrdv);
    }

    @PutMapping("/update/{idrdv}")
    public String update(@PathVariable Long idrdv, @RequestBody RendezVous rendezVous) {
         rendezVousService.modifier(idrdv, rendezVous);
        return "Rendez-vous modifié avec succès";
    }

    @GetMapping("/mesrendezvous/{id_patient}")
    public  List<RendezVous> lister(@PathVariable("id_patient") Patient patient){
        List<RendezVous> rendezVousList= rendezVousRepository.findByPatient(patient);
        for (int k = 0, j = rendezVousList.size() - 1; k < j; k++)
        {
            rendezVousList.add(k, rendezVousList.remove(j));
        }

        return rendezVousList;
    }

    @GetMapping("/mesrendezvousprof/{id_professionnel}")
    public  List<RendezVous> lister(@PathVariable("id_professionnel") Professionnel professionnel){
       List<RendezVous> rendezVousList=  rendezVousRepository.findByProfessionnel(professionnel);
        for (int k = 0, j = rendezVousList.size() - 1; k < j; k++)
        {
            rendezVousList.add(k, rendezVousList.remove(j));
        }

        return rendezVousList;
    }



    @GetMapping("/patients/{professionnelId}")
    public List<Patient> getPatientsForProfessional(@PathVariable Long professionnelId) {
        List<RendezVous> appointments = rendezVousRepository.findByProfessionnelId(professionnelId);
        List<Patient> patients = new ArrayList<>();
        for (RendezVous appointment : appointments) {
            patients.add(appointment.getPatient());
        }
        return patients;
    }



   /*@GetMapping("/touslespatients/{professionnelId}")
    public List<Patient> getPatientsForProf(@PathVariable Long professionnelId) {
        return rendezVousRepository.findAllPatientsForProfessionnel(professionnelId);

    }*/

//;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



    // Getters and setters omitted for brevity

 //@Modifying
    //@Transactional
    @GetMapping("troverrendezvousparid/{id}")
    public RendezVous TrouverPatientparId(@PathVariable("id") Long id){
        return rendezVousRepository.findById(id).get();
    }


    @GetMapping("troverpatientsparidprof/{id}")
    public List<Object> TroverPatientsforProf(@PathVariable Long id){
        return rendezVousRepository.findAllPatientsForProfessionnel(id);
    }

}


