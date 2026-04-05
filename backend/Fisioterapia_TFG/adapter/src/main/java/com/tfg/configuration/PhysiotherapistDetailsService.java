package com.tfg.configuration;

import com.tfg.exceptions.InvalidIdException;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.physiotherapist.PhysiotherapistId;
import com.tfg.port.out.persistence.PhysiotherapistRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class PhysiotherapistDetailsService implements UserDetailsService {
    private PhysiotherapistRepository physiotherapistRepository;

    public PhysiotherapistDetailsService(PhysiotherapistRepository physiotherapistRepository) {
        this.physiotherapistRepository = physiotherapistRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String physioId) throws UsernameNotFoundException {
        PhysiotherapistId physiotherapistId = new PhysiotherapistId(Integer.parseInt(physioId));
        Physiotherapist physiotherapist = physiotherapistRepository.findById(physiotherapistId)
                .orElseThrow(InvalidIdException::new);

        return new PhysiotherapistDetails(physiotherapist);
    }
}
