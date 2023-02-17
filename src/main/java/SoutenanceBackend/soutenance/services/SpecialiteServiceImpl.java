package SoutenanceBackend.soutenance.services;

import SoutenanceBackend.soutenance.Models.Specialite;
import SoutenanceBackend.soutenance.Repository.SpecialiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class SpecialiteServiceImpl implements SpecialiteService {
    @Autowired
    SpecialiteRepository specialiteRepository;

    @Override
    public Object ajouter(Specialite specialite) {
        Specialite specialite1 = specialiteRepository.findByLibelle(specialite.getLibelle());
        if (specialite1==null){
            specialiteRepository.save(specialite);
            return "Spécialité ajoutée avec succès";
        }else {
            return "Cette specialité existe déjà";
        }

    }

    @Override
    public List<Specialite> afficher() {
        return specialiteRepository.findAll();
    }

    @Override
    public String modifier(Long idspec,Specialite specialite) {
        return specialiteRepository.findById(idspec)
                .map(p->{
                    p.setLibelle(specialite.getLibelle());

                    specialiteRepository.save(p);
                    return "Specialité modifiée avec succès !!";
                }).orElseThrow(() -> new RuntimeException("Specialité non trovée!!"));
    }

    @Override
    public String supprimer(Long idspec ) {
        specialiteRepository.deleteById(idspec);
        return"Specialité supprimée avec succès !!";
    }



}
