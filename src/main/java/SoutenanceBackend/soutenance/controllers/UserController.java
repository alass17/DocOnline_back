package SoutenanceBackend.soutenance.controllers;

import SoutenanceBackend.soutenance.Models.Notification;
import SoutenanceBackend.soutenance.Models.Patient;
import SoutenanceBackend.soutenance.Models.User;
import SoutenanceBackend.soutenance.Repository.UserRepository;
import SoutenanceBackend.soutenance.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(value = {"http://localhost:8100","http://localhost:4200"},maxAge = 3600,allowCredentials = "true")
@RestController
@RequestMapping("/user")
public class UserController {


        //private static final Logger LOG = Logger.getLogger(AuthController.class.getName());
        @Autowired
        private UserService userService;

        @Autowired
    UserRepository userRepo;


        // µµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµ

        //@PreAuthorize("hasRole('USER') or hasRole('COLLABORATEUR') or hasRole('ADMIN')")
        @GetMapping("/afficher")
        public List<User> AfficherUsers(){
            // LOG.info("userService.Afficher()");
            return userService.Afficher();
        }

        // µµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµµ   MODIFIER
        //@PreAuthorize("hasRole('ADMIN')")
        @PutMapping("/modifier/{id}")
        public String ModierUser(@RequestBody User users ,@PathVariable Long id){

            userService.Modifier(id,users);
            // LOG.info("Modification reussie avec succès");
            return "Modification reussie avec succès";
        }

        @PreAuthorize("hasRole('ADMIN')")
        @DeleteMapping("/Supprimer/{id_users}")
        public String Supprimer(@PathVariable("id_users") Long id_users){
            userService.Supprimer(id_users);
            //LOG.info("Suppression reussie");
            return "Suppression reussie";
        }

        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping ("/ajouter")
        public String Ajouter(@RequestBody User utilisateur){
            userService.Ajouter(utilisateur);
            // LOG.info("Ajouter avec succès");
            return "Ajouter avec succès";
        }

   @GetMapping("/trouverUserparId/{id}")
    public User Ajouter(@PathVariable Long id){

        // LOG.info("Ajouter avec succès");
        return userRepo.findById(id).get();
    }

}
