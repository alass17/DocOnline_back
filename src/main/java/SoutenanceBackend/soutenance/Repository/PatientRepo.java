package SoutenanceBackend.soutenance.Repository;

import SoutenanceBackend.soutenance.Models.Patient;
import SoutenanceBackend.soutenance.Models.Professionnel;
import SoutenanceBackend.soutenance.Models.RendezVous;
import SoutenanceBackend.soutenance.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepo extends JpaRepository <Patient,Long> {

    Boolean existsByNumero(String numero);
    Boolean existsByNom(String nom);
    Boolean existsByEmail(String email);
    Professionnel findByAdresse(String adresse);

 //   List<Patient> findPatientByProfessionnel(Long id_professionnel);


    @Query(value = "SELECT DISTINCT users.* FROM users, patient, professionnel, rdv " +
            "WHERE patient.id = rdv.patient_id " +
            "AND users.id = patient.id " +
            "AND rdv.professionnel_id = :professionnelId", nativeQuery = true)
    List<User> findPatientsByProfessionnel(@Param("professionnelId") int professionnelId);


    Optional<Patient> findById(Long id_patient);




}
