package SoutenanceBackend.soutenance.services;

import SoutenanceBackend.soutenance.Models.ObjetRdv;
import SoutenanceBackend.soutenance.Models.RendezVous;
import SoutenanceBackend.soutenance.Repository.ObjetRdvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObjetRdvServiceImpl implements ObjetRdvService{
    @Autowired
    private ObjetRdvRepository objetRdvRepository;

    @Override
    public Object ajouter(ObjetRdv objetRdv) {
        ObjetRdv objetRdv1=objetRdvRepository.findByLibelle(objetRdv.getLibelle());

        if (objetRdv1==null){
            return objetRdvRepository.save(objetRdv);

        }else {
            return "Cet objet existe deja";
        }

    }

    @Override
    public String supprimer(Long idobjet) {
       objetRdvRepository.deleteById(idobjet);
        return "Supprimé avec succès";
    }

    @Override
    public List<ObjetRdv> lister() {
        return objetRdvRepository.findAll();
    }

    @Override
    public String modifier(Long idrdv, ObjetRdv objetRdv) {
        return objetRdvRepository.findById(idrdv)
                .map(p->{
                    p.setLibelle(objetRdv.getLibelle());

                    objetRdvRepository.save(p);
                    return "Rendez-vous modifié avec succès !!";
                }).orElseThrow(() -> new RuntimeException("Rendez-vous non trové!"));
    }
}
