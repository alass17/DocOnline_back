package SoutenanceBackend.soutenance;

import SoutenanceBackend.soutenance.Models.FuseauHoraire;
import SoutenanceBackend.soutenance.Models.Role;
import SoutenanceBackend.soutenance.Models.User;
import SoutenanceBackend.soutenance.Repository.FuseauHoraireRepo;
import SoutenanceBackend.soutenance.Repository.RoleRepository;
import SoutenanceBackend.soutenance.Repository.UserRepository;
import SoutenanceBackend.soutenance.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static SoutenanceBackend.soutenance.Models.ERole.*;

@SpringBootApplication
//@EnableScheduling
public class SoutenanceApplication implements CommandLineRunner {
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	 UserService userService;

	@Autowired
	FuseauHoraireRepo fuseauHoraireRepo;

	public static void main(String[] args) {
		SpringApplication.run(SoutenanceApplication.class, args);
	}

	/*@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			Role r1 = userService.saveRole(new Role(null, ROLE_ADMIN));
			Role r2 = userService.saveRole(new Role(null, ROLE_USER));
			if(userRepository.findAll().size()==0 ) {


				User u1 = userService.saveUser(new User(null, "Malle Alassane", "70804808", "alassanemalle733@gmail.com", "1234", "1234", "Magnambougou", new HashSet<>()));

				userService.addRoleToUser(u1.getNumero(), r1.getName());

			}
		};
	}*/
	@Primary
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void run(String... args) throws Exception {
		if (roleRepository.findAll().size() == 0) {
			Role r1 = userService.saveRole(new Role(null, ROLE_ADMIN));
			Role r2 = userService.saveRole(new Role(null, ROLE_USER));
			Role r3 = userService.saveRole(new Role(null, ROLE_PROFESSIONNEL));
			Role r4 = userService.saveRole(new Role(null, ROLE_PATIENT));

			if (userRepository.findAll().size() == 0) {


				User u1 = userService.saveUser(new User(null,"Malle Alassane", "70804808", "alassanemalle733@gmail.com", "12345678", "12345678", "Magnambougou", new HashSet<>()));

				userService.addRoleToUser(u1.getNumero(), r1.getName());

			}

		}

		// HORAIRE DU CALENDRIER
		Set<FuseauHoraire> calendrierSet = new HashSet<>();
		FuseauHoraire calendrier1 = new FuseauHoraire(1L, LocalTime.of(8, 0), LocalTime.of(9, 0));
		FuseauHoraire calendrier2 = new FuseauHoraire(2L, LocalTime.of(9, 0), LocalTime.of(10, 0));
		FuseauHoraire calendrier3 = new FuseauHoraire(3L, LocalTime.of(10, 0), LocalTime.of(11, 0));
		FuseauHoraire calendrier4 = new FuseauHoraire(4L, LocalTime.of(11, 0), LocalTime.of(12, 0));
		FuseauHoraire calendrier5 = new FuseauHoraire(5L, LocalTime.of(12, 0), LocalTime.of(13, 0));
		FuseauHoraire calendrier6 = new FuseauHoraire(6L, LocalTime.of(13, 0), LocalTime.of(14, 0));
		FuseauHoraire calendrier7 = new FuseauHoraire(7L, LocalTime.of(14, 0), LocalTime.of(15, 0));
		FuseauHoraire calendrier8 = new FuseauHoraire(8L, LocalTime.of(15, 0), LocalTime.of(16, 0));
		FuseauHoraire calendrier9 = new FuseauHoraire(9L, LocalTime.of(16, 0), LocalTime.of(17, 0));
		calendrierSet.add(calendrier1);
		calendrierSet.add(calendrier2);
		calendrierSet.add(calendrier3);
		calendrierSet.add(calendrier4);
		calendrierSet.add(calendrier5);
		calendrierSet.add(calendrier6);
		calendrierSet.add(calendrier7);
		calendrierSet.add(calendrier8);
		calendrierSet.add(calendrier9);
		this.fuseauHoraireRepo.saveAll(calendrierSet);
	}
}
