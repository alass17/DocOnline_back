package SoutenanceBackend.soutenance.Repository;

import SoutenanceBackend.soutenance.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByNumero(String numero);

    Optional<User> findByNumeroOrEmail(String numero, String email);

    Boolean existsByNumero(String numero);

    Boolean existsByEmail(String email);


    List<User> findByNumeroContaining(String numero);

    User findByEmail(String email);

    Optional<User> findById(Long id);

/*    @Query("SELECT user FROM User user WHERE user.id=:x")
    public User findUserById(@Param("x") Long id);*/
}
