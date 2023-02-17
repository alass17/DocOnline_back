package SoutenanceBackend.soutenance.controllers;


import SoutenanceBackend.soutenance.Models.ERole;
import SoutenanceBackend.soutenance.Models.Role;
import SoutenanceBackend.soutenance.Models.User;
import SoutenanceBackend.soutenance.Repository.RoleRepository;
import SoutenanceBackend.soutenance.Repository.UserRepository;
import SoutenanceBackend.soutenance.Security.jwt.JwtUtils;
import SoutenanceBackend.soutenance.request.LoginRequest;
import SoutenanceBackend.soutenance.request.SignupRequest;
import SoutenanceBackend.soutenance.response.JwtResponse;
import SoutenanceBackend.soutenance.response.MessageResponse;
import SoutenanceBackend.soutenance.services.UserDetailsImpl;
import SoutenanceBackend.soutenance.services.UserService;
import SoutenanceBackend.soutenance.util.EmailConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(value = {"http://localhost:8100","http://localhost:4200"}, maxAge = 3600,allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:8100")
public class AuthController {

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


  @PostMapping("/signin")
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



  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByNumero(signUpRequest.getNumero())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Ce numero est deja pris!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Cet email est deja pris!"));
    }


    // Create new user's account
    if (signUpRequest.getPassword().equals(signUpRequest.getConfirmpassword())){
      User user = new User(signUpRequest.getNom(),signUpRequest.getNumero(),
              signUpRequest.getEmail(),
              encoder.encode(signUpRequest.getPassword()), encoder.encode(signUpRequest.getConfirmpassword()),
              signUpRequest.getAdresse());
      Set<String> strRoles = signUpRequest.getRole();
      Set<Role> roles = new HashSet<>();

      if (strRoles == null) {
        Role userRole = roleRepository.findByName(ERole.ROLE_USER);
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

      user.setRoles(roles);
      userRepository.save(user);
      mailSender.send(emailConstructor.constructNewUserEmail(user));

      return ResponseEntity.ok(new MessageResponse("Utilisateur enregistré avec succès!"));
    } else {
      return ResponseEntity.badRequest().body(new MessageResponse("Les mots de passe ne sont pas les mêmes"));
    }

  }

  //::::::::::::::::::::::::::::::REINITIALISER PASSWORD::::::::::::::::::::::::::::::::::::::::::://

  @GetMapping("/resetpassword/{email}")
  public ResponseEntity<String> resetPassword(@PathVariable("email") String email) {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      return new ResponseEntity<String>("Email non fourni", HttpStatus.BAD_REQUEST);
    }
    userService.resetPassword(user);
    return new ResponseEntity<String>("Email envoyé!", HttpStatus.OK);
  }

  //::::::::::::::::::::::::::::::::::::::::Changer mot de passe:::::::::::::::::::::::::::::::::::::::::::::::://

    @PostMapping("/changePassword/{numero}")
    public ResponseEntity<String> changePassword(@RequestBody HashMap<String, String> request, @PathVariable String numero) {
        //String numeroOrEmail = request.get("numeroOrEmail");
        User user = userRepository.findByNumero(numero);
        if (user == null) {
            return new ResponseEntity<>("Utilisateur non fourni!", HttpStatus.BAD_REQUEST);
        }
        String currentPassword = request.get("currentpassword");
        String newPassword = request.get("newpassword");
        String confirmpassword = request.get("confirmpassword");
        System.out.println(currentPassword+" current passworddddddddddddddd");
        if (!newPassword.equals(confirmpassword)) {
            return new ResponseEntity<>("PasswordNotMatched", HttpStatus.BAD_REQUEST);
        }
        String userPassword = user.getPassword();
        System.out.println(userPassword+" current passworddddddddddddddd encrypttttttttttttt");

        try {
            if (newPassword != null && !newPassword.isEmpty() && !StringUtils.isEmpty(newPassword)) {
                if (bCryptPasswordEncoder.matches(currentPassword, userPassword)) {
                    userService.updateUserPassword(user, newPassword);
                    System.out.println(newPassword+" newPassword passworddddddddddddddd");

                }
            } else {
                return new ResponseEntity<>("IncorrectCurrentPassword", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Mot de passe changé avec succès!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error Occured: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    //::::::::::::::::::::::::::::::Afficher tous les users::::::::::::::::::::::::::::::::::::::::::://

    @GetMapping("/afficher")
    public List<User> afficher() {
        return userRepository.findAll();

    }
}

