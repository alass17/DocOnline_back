package SoutenanceBackend.soutenance.DtoViewModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class TodayAppointmentListDto {
    private Long appointmentId;
    private String patientName;
    private String patientEmail;
    private Long shift_horaire_id;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private boolean status;
}
