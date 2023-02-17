package SoutenanceBackend.soutenance.Repository;

import SoutenanceBackend.soutenance.Models.ERole;
import SoutenanceBackend.soutenance.Models.Patient;
import SoutenanceBackend.soutenance.Models.Professionnel;
import SoutenanceBackend.soutenance.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessionnelRepo extends JpaRepository <Professionnel ,Long> {
    Boolean existsByNumero(String numero);
    Boolean existsByNom(String nom);
    Boolean existsByEmail(String email);
    Professionnel findByAdresse(String adresse);

}
