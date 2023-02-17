package SoutenanceBackend.soutenance.Repository;

import SoutenanceBackend.soutenance.Models.FuseauHoraire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
@Repository
public interface FuseauHoraireRepo extends JpaRepository<FuseauHoraire,Long> {
    Boolean existsByTimeStart(String timeStart);

    Boolean existsByTimeEnd(String timeEnd);

    FuseauHoraire findByTimeStartAndTimeEnd(LocalTime timeStart, LocalTime timeEnd);
}
