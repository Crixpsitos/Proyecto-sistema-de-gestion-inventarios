package gestion_inventarios.backend.infrastructure.in.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import gestion_inventarios.backend.application.ports.in.FindUserCase;
import gestion_inventarios.backend.domain.exception.UserNotFoundException;

@Configuration
public class SecurityBeansInyector {

    private final FindUserCase findUserCase;

    public SecurityBeansInyector(FindUserCase findUserCase) {
        this.findUserCase = findUserCase;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            try {
                return new UserDetailsAdapter(findUserCase.findByEmail(email));
            } catch (UserNotFoundException e) {
                throw new UsernameNotFoundException(e.getMessage(), e);
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

