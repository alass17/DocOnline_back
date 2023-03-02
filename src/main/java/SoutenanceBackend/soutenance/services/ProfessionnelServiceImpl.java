package SoutenanceBackend.soutenance.services;

import SoutenanceBackend.soutenance.DtoMapper.RendezVousMapper;
import SoutenanceBackend.soutenance.DtoViewModels.Request.TodayAppointmentRequest;
import SoutenanceBackend.soutenance.DtoViewModels.Responses.ApiResponse;
import SoutenanceBackend.soutenance.Models.Professionnel;
import SoutenanceBackend.soutenance.Models.User;
import SoutenanceBackend.soutenance.Repository.FuseauHoraireRepo;
import SoutenanceBackend.soutenance.Repository.PatientRepo;
import SoutenanceBackend.soutenance.Repository.ProfessionnelRepo;
import SoutenanceBackend.soutenance.Repository.RendezVousRepository;
import SoutenanceBackend.soutenance.util.TweakResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ProfessionnelServiceImpl implements ProfesionnelService{

    private FuseauHoraireRepo fuseauHoraireRepo;
    @Autowired
    RendezVousRepository rendezVousRepository;
    @Autowired
    RendezVousMapper rendezVousMapper;
    @Autowired
    PatientRepo patientRepository;
    @Autowired
    TweakResponse tweakResponse;
 /*   public void ProfessionenelService(ProfessionnelRepo professionnelRepo, PatientRepo patientRepository, FuseauHoraireRepo fuseauHoraireRepo, RendezVousRepository rendezVousRepository, RendezVousMapper rendezVousMapper, TweakResponse tweakResponse) {
        this.professionnelRepo = professionnelRepo;
        this.patientRepository = patientRepository;
        this.fuseauHoraireRepo = fuseauHoraireRepo;
        this.rendezVousRepository = rendezVousRepository;

        this.rendezVousMapper = rendezVousMapper;
        this.tweakResponse = tweakResponse;
    }*/
    @Autowired
    ProfessionnelRepo professionnelRepo;
    @Override

    public List<Professionnel> lister(Professionnel professionnel) {
        return professionnelRepo.findAll();
    }

    @Override
    public Object Modifier(Long id, Professionnel professionnel) {
        return professionnelRepo.findById(id)
                .map(p->{
                    p.setNom(professionnel.getNom());
                    p.setNumero(professionnel.getNumero());
                    p.setEmail(professionnel.getEmail());
                    p.setAdresse(professionnel.getAdresse());
                    p.setImageprofil(professionnel.getImageprofil());

                    professionnelRepo.save(p);
                    return "Professionnel modifiée avec succès !!";
            }).orElseThrow(() -> new RuntimeException("Professionnel non trovée!!"));
    }


    public Object AllMedecinAvailability(UserDetailsImpl currentUser,@RequestBody TodayAppointmentRequest todayAppointment){
        var medecin=this.professionnelRepo.findById(todayAppointment.getMedecinId());
        var date = LocalDate.parse(todayAppointment.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if(medecin.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false,"Medecin not found."),
                    HttpStatus.NOT_FOUND);
        }
        var isAvailable =this.rendezVousRepository.findAllByProfessionnelAndDate(medecin.get(), date);
        if(isAvailable.isEmpty()){
//            return new ResponseEntity<>(new ApiResponse(false,"No event for today."),
//                    HttpStatus.NOT_FOUND);
            return null;
        }
        int size = isAvailable.size();
        var response = this.rendezVousMapper.toDto(isAvailable, isAvailable.get(0), size);
        return response;
    }




    // Delete Event.
    @Override
    public Object deleteEvent(String rendezVousId) {
        var appointment = this.rendezVousRepository.findById(Long.parseLong(rendezVousId));
        if(appointment.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false,"Rendez-vous non fourni."),
                    HttpStatus.NOT_FOUND);
        }
        this.rendezVousRepository.deleteById(Long.parseLong(rendezVousId));
        // Send Email
//        this.mailSender.notifyPatient(false, appointment.get().getMedecin().getFullName() ,appointment.get().getPatient().getEmail(), appointment.get().getDate(), appointment.get().getShiftHoraire().getTimeStart(), appointment.get().getShiftHoraire().getTimeEnd());

        return new ResponseEntity<>(new ApiResponse(true,"Event supprimer avec success"),
                HttpStatus.OK);
    }

    // Change Event Status :
    public Object changeEventStatus(String appointmentId){
        var appointment = this.rendezVousRepository.findById(Long.parseLong(appointmentId));

        if(appointment.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(false,"Appointment not found."),
                    HttpStatus.NOT_FOUND);
        }
        var newAppointmentStatus = !appointment.get().isActive();
        appointment.get().setActive(newAppointmentStatus);

        this.rendezVousRepository.save(appointment.get());

        // Send Email
//        this.mailSender.notifyPatient(newAppointmentStatus, appointment.get().getMedecin().getFullName() ,appointment.get().getPatient().getEmail(), appointment.get().getDate(), appointment.get().getShiftHoraire().getTimeStart(), appointment.get().getShiftHoraire().getTimeEnd());
        return new ResponseEntity<>(new ApiResponse(true,"Event modifier avec success"),
                HttpStatus.OK);
    }



}
