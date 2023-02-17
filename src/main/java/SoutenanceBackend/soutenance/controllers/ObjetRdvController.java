package SoutenanceBackend.soutenance.controllers;

import SoutenanceBackend.soutenance.Models.ObjetRdv;
import SoutenanceBackend.soutenance.services.ObjetRdvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/objet")
@CrossOrigin(value = {"http://localhost:8100","http://localhost:4200"},maxAge = 3600 , allowCredentials = "true")

@RestController

public class ObjetRdvController {

    @Autowired
    ObjetRdvService objetRdvService;


    @PostMapping("/ajouter")
    private Object create(@RequestBody ObjetRdv objetRdv){
        return objetRdvService.ajouter(objetRdv);

    }

    @GetMapping("/lister")
    private List<ObjetRdv> read(){
        return objetRdvService.lister();

    }

    @DeleteMapping("/supprimer/{idobjet}")
    private String delete (@PathVariable Long idobjet){
        return objetRdvService.supprimer(idobjet);

    }

    @PutMapping("/update/{idobjet}")
    private String delete (@PathVariable Long idobjet ,@RequestBody ObjetRdv objetRdv){
       objetRdvService.modifier(idobjet, objetRdv);
        return "Objet modifié avec succès";

    }
}
