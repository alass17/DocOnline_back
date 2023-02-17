package SoutenanceBackend.soutenance.DtoViewModels.Request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NewAppointmentRequest {
   /* private String name;
    private String email;*/
    private String date;
    private Long shiftTimeId;
    private Long medecinId;
    private boolean isValide = false;
}
