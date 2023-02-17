package SoutenanceBackend.soutenance.services;

import SoutenanceBackend.soutenance.DtoMapper.RendezVousMapper;
import SoutenanceBackend.soutenance.DtoViewModels.AvailabilityMedecinResponse;
import SoutenanceBackend.soutenance.DtoViewModels.Request.MedecinAvailabilityRequest;
import SoutenanceBackend.soutenance.DtoViewModels.Request.NewAppointmentRequest;
import SoutenanceBackend.soutenance.DtoViewModels.Responses.ApiResponse;
import SoutenanceBackend.soutenance.Models.Patient;
import SoutenanceBackend.soutenance.Models.Professionnel;
import SoutenanceBackend.soutenance.Models.RendezVous;
import SoutenanceBackend.soutenance.Repository.*;
import SoutenanceBackend.soutenance.util.TweakResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PublicService {


    private ProfessionnelRepo professionnelRepo;
    private PatientRepo patientRepository;
    private FuseauHoraireRepo fuseauHoraireRepo;
    private RendezVousRepository rendezVousRepo;
    private ObjetRdvRepository objetRdvRepository;

    private RendezVousMapper appointementMapper;
    private TweakResponse tweakResponse;
    @Autowired
    public PublicService(ProfessionnelRepo professionnelRepo, PatientRepo patientRepository, FuseauHoraireRepo fuseauHoraireRepo, RendezVousRepository rendezVousRepo, RendezVousMapper appointementMapper,TweakResponse tweakResponse,ObjetRdvRepository objetRdvRepository) {
        this.professionnelRepo = professionnelRepo;
        this.patientRepository = patientRepository;
        this.fuseauHoraireRepo = fuseauHoraireRepo;
        this.rendezVousRepo = rendezVousRepo;
        this.objetRdvRepository=objetRdvRepository;
        this.appointementMapper = appointementMapper;
        this.tweakResponse = tweakResponse;
    }

    // List all available Medecin
    public List<Professionnel> listAllMedecin(){
        return this.professionnelRepo.findAll();
    }

    // Get list of availability of a doctor by day.
    public List<AvailabilityMedecinResponse> listAllMedecinAvailability(MedecinAvailabilityRequest medecinAvailability){

        var professionnel = this.professionnelRepo.findById(medecinAvailability.getMedecinId());
        var date = LocalDate.parse(medecinAvailability.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if(professionnel.isEmpty()){
            return null;
        }
        var medecinAvailabilities = this.rendezVousRepo.findAllByProfessionnelAndDate(professionnel.get(), date);

        var medecinAvailabilityList =
                medecinAvailabilities.stream().sorted((a1, a2) -> {
                    System.out.printf("sort: %s; %s\n", a1, a2);
                    return a1.getFuseauHoraire().getId().compareTo(a2.getFuseauHoraire().getId());
                })
                        .map(a -> this.appointementMapper.toAvailabilityMedecinDto(a))
                        .collect(Collectors.toList());

        return this.tweakResponse.listAllAvailiblityByStatus(medecinAvailabilityList);
    }

    // Add new appointement;
    public Object save(NewAppointmentRequest newAppointment, Long idpatient,Long idobjet){

        var objet = this.objetRdvRepository.findById(idobjet);

        var patient = this.patientRepository.findById(idpatient);
        var professionnel = this.professionnelRepo.findById(newAppointment.getMedecinId());
        var fuseauHoraire = this.fuseauHoraireRepo.findById(newAppointment.getShiftTimeId());
        var date = LocalDate.parse(newAppointment.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if(professionnel.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false,"Professionnel non trouvé."),
                    HttpStatus.NOT_FOUND);
        }
        if(patient.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false,"Patient non trouvé."),
                    HttpStatus.NOT_FOUND);
        }

        if(fuseauHoraire.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false,"Time shift does not exist, please try again."),
                    HttpStatus.BAD_REQUEST);
        }

        var isAvailable = this.rendezVousRepo.findByProfessionnelAndDateAndFuseauHoraire(professionnel.get(), date, fuseauHoraire.get());
        // Check if not already take.
        if(isAvailable.isPresent()){
            return new ResponseEntity<>(new ApiResponse(false,"Time shift already taken, please choose another one."),
                    HttpStatus.BAD_REQUEST);
        }
       /* var patientNom = newAppointment.getName();
        var patientEmail = newAppointment.getEmail();*/

        // Finally perform the save operation
        //Patient patient = new Patient(patientNom,patientEmail);
        //Patient savedPatient = this.patientRepository.save(patient);
        RendezVous rendezVous = new RendezVous( date, false,professionnel.get(), patient.get() ,objet.get(),fuseauHoraire.get());
        this.rendezVousRepo.save(rendezVous);

        return rendezVous;
    }
}
