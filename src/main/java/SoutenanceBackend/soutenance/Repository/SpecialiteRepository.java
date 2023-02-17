package SoutenanceBackend.soutenance.Repository;

import SoutenanceBackend.soutenance.Models.Specialite;
import SoutenanceBackend.soutenance.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository

public interface SpecialiteRepository extends JpaRepository<Specialite, Long> {


    Specialite findByLibelle(String libelle);
}
