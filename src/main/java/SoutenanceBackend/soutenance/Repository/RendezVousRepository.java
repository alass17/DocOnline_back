package SoutenanceBackend.soutenance.Repository;

import SoutenanceBackend.soutenance.Models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous,Long> {

    //List<RendezVous> findByDateAndProfessionnelAndPatient(LocalDate date, Professionnel professionnel, Patient patient);
    List<RendezVous> findByPatient(Patient patient);

    List<RendezVous> findAllByProfessionnelAndDate(Professionnel professionnel, LocalDate date);

    Optional<RendezVous> findByProfessionnelAndDateAndFuseauHoraire(Professionnel professionnel, LocalDate date, FuseauHoraire fuseauHoraire);

    List<RendezVous> findByProfessionnelId(Long professionnelId);


    List<RendezVous> findByPatientId(Long patientId);

    /*@Modifying
    @Transactional
    @Query( value = "SELECT DISTINCT users.nom FROM users, patient ,professionnel,rdv WHERE patient.id=rdv.patient_id AND users.id=patient.id AND  rdv.professionnel_id=:id_professionnel", nativeQuery=true)
    List<User> allpatientforprofessionnel(Long id_professionnel);
*/
    //@Modifying
    //@Transactional
    @Query(value = "SELECT DISTINCT patient.* FROM patient ,professionnel,rdv WHERE patient.id=rdv.patient_id AND  rdv.professionnel_id=:id_professionnel", nativeQuery = true)
    List <Object> findAllPatientsForProfessionnel(Long id_professionnel);

    List<RendezVous> findByProfessionnel(Professionnel professionnel);
}
    /*@Modifying
    @Transactional
    @Query(value = "SELECT DISTINCT patient.* FROM patient, professionnel, rdv WHERE patient.id=rdv.patient_id AND rdv.professionnel_id=:id_professionnel",
            nativeQuery=true)
    @org.hibernate.annotations.Loader(namedQuery = "findAllPatientsForProfessionnel")
    @org.hibernate.annotations.ResultTransformer(
            org.hibernate.transform.AliasToBeanResultTransformer.class
    )
    List<Patient>findAllPatientsForProfessionnel(Long id_professionnel);
}
*/