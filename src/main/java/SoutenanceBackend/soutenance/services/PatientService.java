package SoutenanceBackend.soutenance.services;

import SoutenanceBackend.soutenance.Models.Patient;
import SoutenanceBackend.soutenance.Models.Professionnel;

import java.util.List;
import java.util.Optional;

public interface PatientService {


    List<Professionnel> trouverProfessionnelParAdressse (String adresse);

    List<Professionnel>TrouverTousLesProfessionnels();

    Optional<Professionnel> TrouverProfessionnelParId(Long idprof);

    Patient trouverParId(Long idpatient);



}
