package SoutenanceBackend.soutenance.util;

import SoutenanceBackend.soutenance.DtoViewModels.AvailabilityMedecinResponse;
import SoutenanceBackend.soutenance.Repository.FuseauHoraireRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TweakResponse {

    @Autowired
    private FuseauHoraireRepo shiftHoraireRepository;

    public List<AvailabilityMedecinResponse> listAllAvailiblityByStatus(List<AvailabilityMedecinResponse> availabilityMedecinList){
        int shiftNb = this.shiftHoraireRepository.findAll().size()+1;

        for(long i = 1 ; i < shiftNb; i++){
            Long j = Long.valueOf(i);
            var shift = this.shiftHoraireRepository.getOne(j);
            AvailabilityMedecinResponse dummy = new AvailabilityMedecinResponse(shift.getId(), shift.getTimeStart() +" - "+shift.getTimeEnd(), true);
            if (!availabilityMedecinList.contains(dummy))
            {
                availabilityMedecinList.add(dummy);
            }
        }
//        return availabilityMedecinList;
        return availabilityMedecinList.stream()
                .sorted(Comparator.comparing(AvailabilityMedecinResponse::getId))
                .collect(Collectors.toList());
    }

}
