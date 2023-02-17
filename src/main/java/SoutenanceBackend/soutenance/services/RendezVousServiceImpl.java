package SoutenanceBackend.soutenance.services;

import SoutenanceBackend.soutenance.Models.Notification;
import SoutenanceBackend.soutenance.Models.Patient;
import SoutenanceBackend.soutenance.Models.Professionnel;
import SoutenanceBackend.soutenance.Models.RendezVous;
import SoutenanceBackend.soutenance.Repository.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RendezVousServiceImpl implements RendezVousService{
    @Autowired
    private RendezVousRepository rendezVousRepository;



    @Override
    public String ajouter(RendezVous rendezVous) {
        /*rendezVous.setProfessionnel(id_professionnel);
        rendezVous.setPatient(id_patient);*/
        rendezVousRepository.save(rendezVous);
        return "Rendez-vous ajouté avce succès !!";
    }

    @Override
    public List<RendezVous> lister() {
        return rendezVousRepository.findAll();
    }

    @Override
    public String modifier(Long idrdv, RendezVous rendezVous) {
        return rendezVousRepository.findById(idrdv)
                .map(p->{
                   // p.setJour(rendezVous.getJour());
                    //p.setHeure(rendezVous.getHeure());

                   rendezVousRepository.save(p);
                    return "Rendez-vous modifié avec succès !!";
                }).orElseThrow(() -> new RuntimeException("Rendez-vous non trové!"));
    }

    @Override
    public String Supprimer(Long idrdv) {
         rendezVousRepository.deleteById(idrdv);
        return "Rendez-vous supprimé avec succès";
    }
}
