package SoutenanceBackend.soutenance.DtoViewModels.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedecinAvailabilityRequest {
    private Long medecinId;
    private String date;
}
