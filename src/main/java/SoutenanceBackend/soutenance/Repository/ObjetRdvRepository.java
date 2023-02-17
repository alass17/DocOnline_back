package SoutenanceBackend.soutenance.Repository;

import SoutenanceBackend.soutenance.Models.ObjetRdv;
import SoutenanceBackend.soutenance.Models.Specialite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjetRdvRepository extends JpaRepository<ObjetRdv, Long> {

    ObjetRdv findByLibelle(String libelle);
}
