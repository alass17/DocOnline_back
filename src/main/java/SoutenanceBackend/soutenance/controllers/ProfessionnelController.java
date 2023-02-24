package SoutenanceBackend.soutenance.controllers;

import SoutenanceBackend.soutenance.DtoViewModels.Request.TodayAppointmentRequest;
import SoutenanceBackend.soutenance.DtoViewModels.Responses.TodayAppointmentResponse;
import SoutenanceBackend.soutenance.Models.*;
import SoutenanceBackend.soutenance.Repository.*;
import SoutenanceBackend.soutenance.Security.jwt.CurrentUser;
import SoutenanceBackend.soutenance.images.ConfigImage;
import SoutenanceBackend.soutenance.request.ProfessionnelRequest;
import SoutenanceBackend.soutenance.response.MessageResponse;
import SoutenanceBackend.soutenance.services.ProfesionnelService;
import SoutenanceBackend.soutenance.services.UserDetailsImpl;
import SoutenanceBackend.soutenance.util.EmailConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@RestController
@CrossOrigin(value = {"http://localhost:8100","http://localhost:4200"},maxAge = 3600 , allowCredentials = "true")
@RequestMapping("/prof")

public class ProfessionnelController {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private EmailConstructor emailConstructor;
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ProfessionnelRepo professionnelRepo;

    @Autowired
    ProfesionnelService profesionnelService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    SpecialiteRepository specialiteRepository;
    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private RendezVousRepository rendezVousRepository;


    @PostMapping("/signup/{idspec}/{lagitude}/{longitude}")
    public ResponseEntity<?> registerUser(@Param("nom") String nom,
                                          @Param("imageprofil") MultipartFile imageprofil,
                                          @Param("numero") String numero,
                                          @Param("email")String email,
                                          @Param("password")String password,
                                          @Param("confirmpassword") String confirmpassword,
                                          @Param("adresse") String adresse,
                                          @Param("document")MultipartFile document,
                                          @PathVariable("longitude")Double longitude,
                                          @PathVariable("lagitude")Double lagitude,
                                          @PathVariable("idspec") Long idspec

                                           ) throws IOException {
        //Professionnel professionnel = new Professionnel();;
        ProfessionnelRequest signUpRequest = new ProfessionnelRequest();
        signUpRequest.setNom(nom);
        signUpRequest.setNumero(numero);
        signUpRequest.setEmail(email);
        signUpRequest.setPassword(password);
        signUpRequest.setConfirmpassword(confirmpassword);
        signUpRequest.setAdresse(adresse);

        signUpRequest.setLongitude(longitude);
        signUpRequest.setLagitude(lagitude);


        String img = StringUtils.cleanPath(document.getOriginalFilename());
        signUpRequest.setDocument(img);
        String uploaDir = "C:\\Users\\amalle\\Desktop\\Soutenance Malle\\Soutenance_Front\\src\\assets\\img";
        ConfigImage.saveimg(uploaDir, img, document);

        String img1 = StringUtils.cleanPath(imageprofil.getOriginalFilename());
        signUpRequest.setImageprofil(img1);
        String uploaDir1 = "C:\\Users\\amalle\\Desktop\\Soutenance Malle\\Soutenance_Front\\src\\assets\\img";
        ConfigImage.saveimg(uploaDir1, img1, imageprofil);


        if (userRepository.existsByNumero(signUpRequest.getNumero())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Ce numero est deja pris!"));
        }

        if (professionnelRepo.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Cet email est deja pris!"));
        }



        // Create new user's account
        if (signUpRequest.getPassword().equals(signUpRequest.getConfirmpassword())){
            Professionnel professionnel = new Professionnel(signUpRequest.getNom(),signUpRequest.getImageprofil(),signUpRequest.getNumero(),
                    signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()),
                    encoder.encode(signUpRequest.getConfirmpassword()),
                    signUpRequest.getAdresse(),
                    signUpRequest.getDocument(),
                    signUpRequest.getLongitude(),
                    signUpRequest.getLagitude());
            Set<String> strRoles = signUpRequest.getRole();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
                Role userRole = roleRepository.findByName(ERole.ROLE_PROFESSIONNEL);
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
                        case "professionnel":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_PROFESSIONNEL);
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
        Specialite specialite =specialiteRepository.findById(idspec).get();

         professionnel.getSpecialites().add(specialite);
            professionnel.setRoles(roles);

            professionnelRepo.save(professionnel);
         //   mailSender.send(emailConstructor.constructNewUserEmail(professionnel));

            return ResponseEntity.ok(new MessageResponse("Professionnel enregistré avec succès!"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Les mots de passe ne sont pas les mêmes"));
        }

    }


    @GetMapping("/lister")
    public List<Professionnel> afficher(){
        return professionnelRepo.findAll();
    }

    @GetMapping("trouverprofparId/{id}")
    public Professionnel TrouverProfparId(@PathVariable("id") Long id){
        return professionnelRepo.findById(id).get();
    }



    @PutMapping("/professionnel/{id}")
    public ResponseEntity<String> updateProfessionnel(@PathVariable Long id,
                                                      @RequestParam("nom") String nom,
                                                      @RequestParam(value = "imageprofil", required = false) MultipartFile imageprofil,
                                                      @RequestParam("numero") String numero,
                                                      @RequestParam("email") String email,
                                                      @RequestParam("adresse") String adresse) throws IOException {
        return professionnelRepo.findById(id)
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
                    professionnelRepo.save(p);
                    return ResponseEntity.ok("Professionnel modifié avec succès !!");
                }).orElseThrow(() -> new RuntimeException("Professionnel non trouvé !!"));
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @PostMapping("events/daily/{id_prof}")
    public ResponseEntity<? extends Object> today( @Valid @RequestBody TodayAppointmentRequest todayAppointment,@PathVariable Long id_prof,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidationException("Appointment has errors; Can not update the status of the appointment;");
        }

        var id_prof1=professionnelRepo.findById(id_prof).get();
        var date = LocalDate.parse(todayAppointment.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        var response = this.profesionnelService.AllMedecinAvailability(todayAppointment,id_prof1.getId());
        if(response==null)
            return ResponseEntity.ok(new TodayAppointmentResponse(date, 0, null));
        return ResponseEntity.ok(response);

    }
    // Delete an appointment
    @DeleteMapping("events/{appointmentId}")
    public ResponseEntity<?> delete(@PathVariable String appointmentId){

        var response =this.profesionnelService.deleteEvent(appointmentId);
        return ResponseEntity.ok(response);
    }

    // Change the status of an event
    @PostMapping("events/status/{appointmentId}")
    public ResponseEntity<?> status(@PathVariable String appointmentId){

        var response = this.profesionnelService.changeEventStatus(appointmentId);
        return ResponseEntity.ok(response);
    }



    /*@GetMapping ("mespatients/id_professionnel")
    public List<Patient> afficherPatientforProfessionnel(@PathVariable Long id_professionnel){
      return   patientRepo.findPatientByProfessionnel(id_professionnel);


    }*/

    //:::::::::::::::::::::::::::::::Afficher les notifications d'un professionnel par id::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @GetMapping("/professionnels/{id}/notifications")
    public List<Notification> getPatientNotifications(@PathVariable Long id) {
        Professionnel professionnel = professionnelRepo.findById(id).get();
        List<Notification> professionnelList= professionnel.getNotifications();

        for (int k = 0, j = professionnelList.size() - 1; k < j; k++)
        {
            professionnelList.add(k, professionnelList.remove(j));
        }
        return  professionnelList;

    }
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    @DeleteMapping("/delete/{id_prof}")
    String supprimer(@PathVariable Long id_prof){
        professionnelRepo.deleteById(id_prof);
        return "Professionnel supprimé avec succès!!";

    }




    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /*@Modifying
    @Transactional
    @GetMapping("/{id_professionnel}/tousmespatients")
    public ResponseEntity<List<Patient>> getAllPatientsForProfessionnel(@PathVariable Long id_professionnel) {
        Optional<Professionnel> optionalProfessionnel = professionnelRepo.findById(id_professionnel);
        if (optionalProfessionnel.isPresent()) {
            List<Patient> patients = rendezVousRepository.findAllPatientsForProfessionnel(id_professionnel);
            return ResponseEntity.ok(patients);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/
}



