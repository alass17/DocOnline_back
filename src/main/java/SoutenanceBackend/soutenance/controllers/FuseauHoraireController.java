package SoutenanceBackend.soutenance.controllers;

import SoutenanceBackend.soutenance.Models.FuseauHoraire;
import SoutenanceBackend.soutenance.Models.ObjetRdv;
import SoutenanceBackend.soutenance.Repository.FuseauHoraireRepo;
import SoutenanceBackend.soutenance.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(value = {"http://localhost:8100","http://localhost:4200"},maxAge = 3600,allowCredentials = "true")

@RestController
@RequestMapping("/fuseau")
public class FuseauHoraireController {

    @Autowired
    FuseauHoraireRepo fuseauHoraireRepo;

    @PostMapping("/save")
    private String Ajouter(@RequestBody  FuseauHoraire fuseauHoraire){
        if (fuseauHoraire.getTimeStart() == null || fuseauHoraire.getTimeEnd() == null) {
            return "timeStart et timeEnd ne peuvent pas être nulls";
        }
        if (fuseauHoraire.getTimeEnd().isBefore(fuseauHoraire.getTimeStart())) {
            return "timeEnd ne peut pas être avant timeStart";
        }
        FuseauHoraire existingFuseauHoraire = fuseauHoraireRepo.findByTimeStartAndTimeEnd(fuseauHoraire.getTimeStart(), fuseauHoraire.getTimeEnd());
        if (existingFuseauHoraire != null) {
            return "Le fuseau horaire avec les mêmes heures de début et de fin existe déjà";
        }


        fuseauHoraireRepo.save(fuseauHoraire);
        return "Fuseau horaire ajouté avec succès";
    }

    @GetMapping("/afficher")
    private List<FuseauHoraire> afficher(){
        return fuseauHoraireRepo.findAll();
    }

    @DeleteMapping("/supprimer/{id}")
    private String Supprimer(@PathVariable Long id){
        fuseauHoraireRepo.deleteById(id);
        return "Fuseau supprimé avec succès !!";
    }
}
