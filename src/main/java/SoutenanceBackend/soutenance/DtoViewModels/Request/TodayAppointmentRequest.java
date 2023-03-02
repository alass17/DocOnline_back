package SoutenanceBackend.soutenance.DtoViewModels.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodayAppointmentRequest {

    // TODO: after setting up login, remove the above row and remplace it by Principal
    private Long medecinId;
    private String date;
}
