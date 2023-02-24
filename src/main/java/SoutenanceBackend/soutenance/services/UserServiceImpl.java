package SoutenanceBackend.soutenance.services;

import SoutenanceBackend.soutenance.Models.ERole;
import SoutenanceBackend.soutenance.Models.Role;
import SoutenanceBackend.soutenance.Models.User;
import SoutenanceBackend.soutenance.Repository.RoleRepository;
import SoutenanceBackend.soutenance.Repository.UserRepository;
import SoutenanceBackend.soutenance.util.EmailConstructor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.authenticator.Constants;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

//@Slf4j
//@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    EmailConstructor emailConstructor;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
     JavaMailSender mailSender;
    @Autowired
     UserRepository userRepo;
    @Autowired
     RoleRepository roleRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public String Supprimer(Long id_users) {
        userRepo.deleteById(id_users);
        return "Supprimer avec succès !!";
    }

    @Override
    public String Modifier(Long id,User users) {
        return userRepo.findById(id)
                .map(p->{
                    p.setNom(users.getNom());
                    p.setNumero(users.getNumero());
                    p.setEmail(users.getEmail());
                    p.setAdresse(users.getAdresse());
                    userRepo.save(p);
                    return "Specialité modifiée avec succès !!";
                }).orElseThrow(() -> new RuntimeException("Specialité non trovée!!"));
    }

    @Override
    public List<User> Afficher() {
        return userRepo.findAll();
    }

    @Override
    public User Ajouter(User utilisateur) {
        return null;
    }

    @Override
    public User saveUser(User user) {
       // log.info("Saving new user {} to the database", user.getNom());
        user.getNumero();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmpassword(passwordEncoder.encode(user.getConfirmpassword()));
        user.getAdresse();


        userRepo.save(user);

        //mailSender.send(emailConstructor.constructNewUserEmail(user));
        return user;


    }

    @Override
    public Role saveRole(Role role) {
        //log.info("Saving new role {} to the database", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String numero, ERole roleName) {
        //log.info("Adding role {} to user {}", numero,roleName);
        User user = userRepo.findByNumero(numero);
        Role role = roleRepo.findByName(roleName);
        user.getRoles().add(role);

    }

    @Override
    public void resetPassword(User user) {
        String password = RandomStringUtils.randomAlphanumeric(10);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        userRepo.save(user);
        mailSender.send(emailConstructor.constructResetPasswordEmail(user, password));

    }

    @Override
    public User findByEmail(String userEmail) {
        return userRepo.findByEmail(userEmail);
    }

    @Override
    public void updateUserPassword(User user, String newpassword) {
        String encryptedPassword = bCryptPasswordEncoder.encode(newpassword);
        user.setPassword(encryptedPassword);
        userRepo.save(user);
        mailSender.send(emailConstructor.constructResetPasswordEmail(user, newpassword));
    }
}
