package com.tfg.configuration;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.physiotherapist.PhysiotherapistEmail;
import com.tfg.port.out.persistence.PhysiotherapistRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class PhysiotherapistDetailsService implements UserDetailsService {
    private final PhysiotherapistRepository physiotherapistRepository;

    public PhysiotherapistDetailsService(PhysiotherapistRepository physiotherapistRepository) {
        this.physiotherapistRepository = physiotherapistRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        PhysiotherapistEmail physiotherapistEmail = new PhysiotherapistEmail(email);
        Physiotherapist physiotherapist = physiotherapistRepository.findByEmail(physiotherapistEmail)
                .orElseThrow(InvalidIdException::new);

        return new PhysiotherapistDetails(physiotherapist);
    }
}
