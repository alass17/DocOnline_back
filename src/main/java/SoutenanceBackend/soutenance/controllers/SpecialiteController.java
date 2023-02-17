package SoutenanceBackend.soutenance.controllers;


import SoutenanceBackend.soutenance.Models.Specialite;
import SoutenanceBackend.soutenance.Repository.SpecialiteRepository;
import SoutenanceBackend.soutenance.response.MessageResponse;
import SoutenanceBackend.soutenance.services.SpecialiteService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor


@RestController
@CrossOrigin(value = {"http://localhost:8100","http://localhost:4200"},maxAge = 3600 , allowCredentials = "true")
@RequestMapping("/specialite")
public class SpecialiteController {
    @Autowired
    SpecialiteService specialiteService;

    @Autowired
    SpecialiteRepository specialiteRepository;


    @PostMapping("/ajouter")
    public Object create(@RequestBody Specialite specialite){
        // Vérification de l'existence de la spécialité par son libellé
        return specialiteService.ajouter(specialite);
    }



    @GetMapping("/afficher")
    public List<Specialite> read(){
        return specialiteService.afficher();
    }
    @DeleteMapping("/supprimer/{idspec}")
    public String delete(@PathVariable Long idspec){
        specialiteService.supprimer(idspec);
        return "Specialité supprimée avec succès";
    }

    @PutMapping("/update/{idspec}")
    public String update(@PathVariable Long idspec,
                         @RequestBody Specialite specialite) {
        specialiteService.modifier(idspec, specialite);
        return "Specialité modifiée avec succès";
    }





}
