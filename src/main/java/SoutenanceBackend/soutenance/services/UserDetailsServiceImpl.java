package SoutenanceBackend.soutenance.services;

import SoutenanceBackend.soutenance.Models.User;
import SoutenanceBackend.soutenance.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String numeroOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByNumeroOrEmail(numeroOrEmail, numeroOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with numero or email: " + numeroOrEmail));

        return UserDetailsImpl.build(user);
    }

}
