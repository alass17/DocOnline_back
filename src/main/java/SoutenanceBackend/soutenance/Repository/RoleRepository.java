package SoutenanceBackend.soutenance.Repository;

import SoutenanceBackend.soutenance.Models.ERole;
import SoutenanceBackend.soutenance.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(ERole name);

    public Role findRoleByName(String name);
}
