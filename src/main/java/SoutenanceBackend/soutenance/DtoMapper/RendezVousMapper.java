package SoutenanceBackend.soutenance.DtoMapper;

import SoutenanceBackend.soutenance.DtoViewModels.AvailabilityMedecinResponse;
import SoutenanceBackend.soutenance.DtoViewModels.TodayAppointmentListDto;
import SoutenanceBackend.soutenance.DtoViewModels.TodayAppointmentResponse;
import SoutenanceBackend.soutenance.Models.RendezVous;
import SoutenanceBackend.soutenance.Repository.ProfessionnelRepo;
import SoutenanceBackend.soutenance.Repository.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RendezVousMapper {

    @Autowired
    private RendezVousRepository rendezVousRepository;
    @Autowired
    private ProfessionnelRepo professionnelRepo;

    public TodayAppointmentResponse toDto(List<RendezVous> rendezVousList, RendezVous rendezVous, int size){

        var TodayAppointmentResponse = new TodayAppointmentResponse();

        TodayAppointmentResponse.setDate(rendezVous.getDate());
        TodayAppointmentResponse.setSize(size);
        TodayAppointmentResponse.setEvents(
                rendezVousList.stream()
                        .sorted((a1, a2) -> {
                            System.out.printf("sort: %s; %s\n", a1, a2);
                            return a1.getFuseauHoraire().getId().compareTo(a2.getFuseauHoraire().getId());
                        })
                        .map(a -> this.test(a))

                        .collect(Collectors.toList())
        );

        return TodayAppointmentResponse;
    }


    public TodayAppointmentListDto test(RendezVous rendezVous){

        var todayAppointmentListDto = new TodayAppointmentListDto();
        todayAppointmentListDto.setAppointmentId(rendezVous.getIdrdv());
        todayAppointmentListDto.setPatientEmail(rendezVous.getPatient().getEmail());
        todayAppointmentListDto.setPatientName(rendezVous.getPatient().getNom());
        todayAppointmentListDto.setStatus(rendezVous.isActive());
        todayAppointmentListDto.setShift_horaire_id(rendezVous.getFuseauHoraire().getId());
        todayAppointmentListDto.setTimeStart(rendezVous.getFuseauHoraire().getTimeStart());
        todayAppointmentListDto.setTimeEnd(rendezVous.getFuseauHoraire().getTimeEnd());
        return todayAppointmentListDto;
    }


    public AvailabilityMedecinResponse toAvailabilityMedecinDto(RendezVous rendezVous){
        var availabilityMedecin = new AvailabilityMedecinResponse();
        availabilityMedecin.setId(rendezVous.getFuseauHoraire().getId());
        availabilityMedecin.setFuseauHoraire(rendezVous.getFuseauHoraire().getTimeStart() +" - "+rendezVous.getFuseauHoraire().getTimeEnd());
        availabilityMedecin.setAvailable(false);
        return availabilityMedecin;
    }


}
