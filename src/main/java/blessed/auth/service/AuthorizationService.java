package blessed.auth.service;

import blessed.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService{

    @Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException{
        return repository.findByEmail(userEmail);
    }

}
