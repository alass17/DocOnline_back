package SoutenanceBackend.soutenance.controllers;

import SoutenanceBackend.soutenance.Models.*;
import SoutenanceBackend.soutenance.Repository.*;
import SoutenanceBackend.soutenance.images.ConfigImage;
import SoutenanceBackend.soutenance.request.PatientRequest;

import SoutenanceBackend.soutenance.response.MessageResponse;
import SoutenanceBackend.soutenance.services.PatientService;
import SoutenanceBackend.soutenance.util.EmailConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin(value = {"http://localhost:8100","http://localhost:4200"},maxAge = 3600,allowCredentials = "true")
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private EmailConstructor emailConstructor;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    PatientService patientService;

    @Autowired
    ProfessionnelRepo professionnelRepo;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PatientRepo patientRepo;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestParam("nom") String nom,
                                          @RequestParam(value = "imageprofil", required = false) MultipartFile imageprofil,
                                          @RequestParam("numero") String numero,
                                          @RequestParam("email")String email,
                                          @RequestParam("password")String password,
                                          @RequestParam("confirmpassword") String confirmpassword,
                                          @RequestParam("adresse") String adresse) throws IOException {
        //Professionnel professionnel = new Professionnel();
        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setNom(nom);
        patientRequest.setNumero(numero);
        patientRequest.setEmail(email);
        patientRequest.setPassword(password);
        patientRequest.setConfirmpassword(confirmpassword);
        patientRequest.setAdresse(adresse);

        if (imageprofil != null) {
            String img1 = StringUtils.cleanPath(imageprofil.getOriginalFilename());
            patientRequest.setImageprofil(img1);
            String uploaDir1 = "C:\\Users\\amalle\\Desktop\\Copie\\Soutenance\\soutenance\\soutenance\\src\\main\\java\\SoutenanceBackend\\soutenance\\images";
            try {
                ConfigImage.saveimg(uploaDir1, img1, imageprofil);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            patientRequest.setImageprofil("avatar.jpg");
        }


        if (userRepository.existsByNumero(patientRequest.getNumero())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Ce numero est deja pris!"));
        }

        if (userRepository.existsByEmail(patientRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Cet email est deja pris!"));
        }



        // Create new user's account
        if (patientRequest.getPassword().equals(patientRequest.getConfirmpassword())){
            Patient patient = new Patient(patientRequest.getImageprofil(),patientRequest.getNom(),patientRequest.getNumero(),
                    patientRequest.getEmail(),
                    encoder.encode(patientRequest.getPassword()), encoder.encode(patientRequest.getConfirmpassword()),
                    patientRequest.getAdresse());
            Set<String> strRoles = patientRequest.getRole();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
                Role userRole = roleRepository.findByName(ERole.ROLE_PATIENT);
                if (userRole==null){
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Error: Role non fourni !"));
                }else {
                    roles.add(userRole);
                }
                //.orElseThrow(() -> new RuntimeException("Error: Role non fournit."));

            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "patient":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_PATIENT);
                            if (adminRole==null){
                                ResponseEntity
                                        .badRequest()
                                        .body(new MessageResponse("Error: Role non fourni !"));
                            }else {
                                roles.add(adminRole);
                            }
                            //.orElseThrow(() -> new RuntimeException("Error: Role non fournit."));
                            break;

                        default:
                            Role userRole = roleRepository.findByName(ERole.ROLE_USER);
                            if (userRole==null){
                                ResponseEntity
                                        .badRequest()
                                        .body(new MessageResponse("Error: Role non fourni !"));
                            }else {
                                roles.add(userRole);
                            }
                            //.orElseThrow(() -> new RuntimeException("Error: Role non fournit."));
                    }
                });
            }

            patient.setRoles(roles);

            patientRepo.save(patient);
            //   mailSender.send(emailConstructor.constructNewUserEmail(patient));

            return ResponseEntity.ok(new MessageResponse("Utilisateur enregistré avec succès!"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Les mots de passe ne sont pas les mêmes"));
        }

    }

    @DeleteMapping("/delete/{id_patient}")
    String supprimer(@PathVariable Long id_patient){
        patientRepo.deleteById(id_patient);
        return "Patient supprimé avec succès!!";

    }

    @GetMapping("/lister")
    public List<Patient> afficher(){
        return patientRepo.findAll();
    }

    @GetMapping("trouverprofparId/{id}")
    public Professionnel TrouverProfparId(@PathVariable("id") Long id){
        return professionnelRepo.findById(id).get();
    }



    @PutMapping("/modifierpatient/{id}")
    public ResponseEntity<String> updatePatient(@PathVariable Long id,
                                                      @RequestParam("nom") String nom,
                                                      @RequestParam(value = "imageprofil", required = false) MultipartFile imageprofil,
                                                      @RequestParam("numero") String numero,
                                                      @RequestParam("email") String email,
                                                      @RequestParam("adresse") String adresse) throws IOException {
        return patientRepo.findById(id)
                .map(p -> {
                    p.setNom(nom);
                    p.setNumero(numero);
                    p.setEmail(email);
                    p.setAdresse(adresse);
                    if (imageprofil != null) {
                        String img1 = StringUtils.cleanPath(imageprofil.getOriginalFilename());
                        p.setImageprofil(img1);
                        String uploaDir1 = "C:\\Users\\amalle\\Desktop\\Copie\\Soutenance\\soutenance\\soutenance\\src\\main\\java\\SoutenanceBackend\\soutenance\\images";
                        try {
                            ConfigImage.saveimg(uploaDir1, img1, imageprofil);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    patientRepo.save(p);
                    return ResponseEntity.ok("Patient modifié avec succès !!");
                }).orElseThrow(() -> new RuntimeException("Professionnel non trouvé !!"));
    }
        @GetMapping("/localiser/{adresse}")
        public List<Professionnel> localiser (@PathVariable("adresse") String adresse){

        return patientService.trouverProfessionnelParAdressse(adresse);

        }

    @GetMapping("/affichertouslesprofessionnel")
    public List<Professionnel> afficherProfessionnel () {

        return patientService.TrouverTousLesProfessionnels();
    }

    @GetMapping("/affichersprofessionnelparId/{id}")
    public Optional<Professionnel> TrouverProfessionnelParId(@PathVariable Long id) {

        return patientService.TrouverProfessionnelParId(id);
    }


    @GetMapping("trouverpatientparId/{id}")
    public Patient TrouverPatientparId(@PathVariable("id") Long id){
        return patientRepo.findById(id).get();
    }



    @GetMapping("/patients/{professionnelId}")
    public List<User> getPatientsByProfessionnel(@PathVariable int professionnelId) {
        return patientRepo.findPatientsByProfessionnel(professionnelId);
    }

    //:::::::::::::::::::::::::::::::Afficher les notifications d'un patient par id::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @GetMapping("/patients/{id}/notifications")
    public List<Notification> getPatientNotifications(@PathVariable Long id) {
        Patient patient = patientRepo.findById(id).get();
        List<Notification> patient1= patient.getNotifications();
        for (int k = 0, j = patient1.size() - 1; k < j; k++)
        {
            patient1.add(k, patient1.remove(j));
        }
        return patient1;
    }
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::


}

