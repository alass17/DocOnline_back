package SoutenanceBackend.soutenance.services;

import SoutenanceBackend.soutenance.Models.ERole;
import SoutenanceBackend.soutenance.Models.Role;
import SoutenanceBackend.soutenance.Models.User;

import java.util.List;

public interface UserService {
    String Supprimer(Long id_users);  // LA METHODE PERMETTANT DE SUPPRIMER UN COLLABORATEUR

    String Modifier(Long id,User users);   // LA METHODE PERMETTANT DE MODIFIER UN COLLABORATEUR

    List<User> Afficher();       // LA METHODE PERMETTANT D'AFFICHER UN COLLABORATEUR

    User Ajouter(User utilisateur); // LA METHODE PERMETTANT D'AJOUTER UN COLLABORATEUR

    User saveUser(User user);
    Role  saveRole(Role role);
    public void addRoleToUser(String numero, ERole roleName);

    public void resetPassword(User user);

    public User findByEmail(String userEmail);
    public void updateUserPassword(User user, String newpassword);

}
