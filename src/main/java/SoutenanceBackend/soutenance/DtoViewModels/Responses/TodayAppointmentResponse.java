package SoutenanceBackend.soutenance.DtoViewModels.Responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TodayAppointmentResponse {
    private LocalDate date;
    private int size;
    private List<TodayAppointmentListDto> events = new ArrayList<>();
}
