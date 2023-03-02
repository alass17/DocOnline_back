package SoutenanceBackend.soutenance.Repository;

import SoutenanceBackend.soutenance.Models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@Repository
public interface ProfessionnelRepo extends JpaRepository <Professionnel ,Long> {
    Boolean existsByNumero(String numero);

    Boolean existsByNom(String nom);

    Boolean existsByEmail(String email);

    Professionnel findByAdresse(String adresse);


    List<Professionnel> findBySpecialites(Specialite specialite);
}