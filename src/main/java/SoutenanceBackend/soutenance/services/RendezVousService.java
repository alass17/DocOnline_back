package SoutenanceBackend.soutenance.services;

import SoutenanceBackend.soutenance.Models.Patient;
import SoutenanceBackend.soutenance.Models.Professionnel;
import SoutenanceBackend.soutenance.Models.RendezVous;
import SoutenanceBackend.soutenance.Models.User;

import java.util.List;

public interface RendezVousService {
    String ajouter (RendezVous rendezVous);
    List<RendezVous> lister();
    String modifier(Long idrdv,RendezVous rendezVous);
    String Supprimer (Long idrdv);
}
