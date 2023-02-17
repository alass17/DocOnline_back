package SoutenanceBackend.soutenance.controllers;

import SoutenanceBackend.soutenance.Models.*;
import SoutenanceBackend.soutenance.Repository.RoleRepository;
import SoutenanceBackend.soutenance.Repository.UserRepository;
import SoutenanceBackend.soutenance.Security.jwt.JwtUtils;
import SoutenanceBackend.soutenance.images.ConfigImage;
import SoutenanceBackend.soutenance.request.LoginRequest;
import SoutenanceBackend.soutenance.request.PatientRequest;
import SoutenanceBackend.soutenance.request.SignupRequest;
import SoutenanceBackend.soutenance.response.JwtResponse;
import SoutenanceBackend.soutenance.response.MessageResponse;
import SoutenanceBackend.soutenance.services.UserDetailsImpl;
import SoutenanceBackend.soutenance.services.UserService;
import SoutenanceBackend.soutenance.util.EmailConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@RestController
@CrossOrigin(value = {"http://localhost:8100","http://localhost:4200"},maxAge = 3600,allowCredentials = "true")

@RequestMapping("/admin")
public class AdminController {


    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailConstructor emailConstructor;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin/admin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getNumeroOrEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getNom(),
                userDetails.getNumero(),
                userDetails.getEmail(),
                userDetails.getAdresse(),
                roles));
    }



    @PostMapping("/signup/admin")
    public ResponseEntity<?> registerAdmin(@RequestParam("nom") String nom,
                                          @RequestParam(value = "imageprofil", required = false) MultipartFile imageprofil,
                                          @RequestParam("numero") String numero,
                                          @RequestParam("email")String email,
                                          @RequestParam("password")String password,
                                          @RequestParam("confirmpassword") String confirmpassword,
                                          @RequestParam("adresse") String adresse) throws IOException {
        //Professionnel professionnel = new Professionnel();
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setNom(nom);
        signupRequest.setNumero(numero);
        signupRequest.setEmail(email);
        signupRequest.setPassword(password);
        signupRequest.setConfirmpassword(confirmpassword);
        signupRequest.setAdresse(adresse);




        if (userRepository.existsByNumero(signupRequest.getNumero())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Ce numero est deja pris!"));
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Cet email est deja pris!"));
        }



        // Create new user's account
        if (signupRequest.getPassword().equals(signupRequest.getConfirmpassword())){
            User admin = new User(signupRequest.getNom(),signupRequest.getNumero(),
                    signupRequest.getEmail(),
                    encoder.encode(signupRequest.getPassword()), encoder.encode(signupRequest.getConfirmpassword()),
                    signupRequest.getAdresse());
            Set<String> strRoles = signupRequest.getRole();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
                Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN);
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
                        case "admin":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
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
                            Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN);
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

            admin.setRoles(roles);

            userRepository.save(admin);
            //   mailSender.send(emailConstructor.constructNewUserEmail(patient));

            return ResponseEntity.ok(new MessageResponse("Utilisateur enregistré avec succès!"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Les mots de passe ne sont pas les mêmes"));
        }

    }

    @GetMapping("trouverUserparId/{id}")
    public User TrouverAdminparId(@PathVariable("id") Long id){
        return userRepository.findById(id).get();
    }

    @PutMapping("/modifieradmin/{id}")
    public ResponseEntity<String> updateAdmin(@PathVariable Long id,
                                                @RequestParam("nom") String nom,
                                                @RequestParam(value = "imageprofil", required = false) MultipartFile imageprofil,
                                                @RequestParam("numero") String numero,
                                                @RequestParam("email") String email,
                                                @RequestParam("adresse") String adresse) throws IOException {
            return userRepository.findById(id)
                .map(p -> {
                    p.setNom(nom);
                    p.setNumero(numero);
                    p.setEmail(email);
                    p.setAdresse(adresse);
                    /*if (imageprofil != null) {
                        String img1 = StringUtils.cleanPath(imageprofil.getOriginalFilename());
                        p.setImageprofil(img1);
                        String uploaDir1 = "C:\\Users\\amalle\\Desktop\\Copie\\Soutenance\\soutenance\\soutenance\\src\\main\\java\\SoutenanceBackend\\soutenance\\images";
                        try {
                            ConfigImage.saveimg(uploaDir1, img1, imageprofil);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }*/
                    userRepository.save(p);
                    return ResponseEntity.ok("Patient modifié avec succès !!");
                }).orElseThrow(() -> new RuntimeException("Professionnel non trouvé !!"));
    }

}