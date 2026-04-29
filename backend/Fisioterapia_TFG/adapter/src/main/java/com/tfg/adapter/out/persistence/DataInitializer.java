package com.tfg.adapter.out.persistence;

import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaDataRepository;
import com.tfg.adapter.out.persistence.physiotherapist.PhysiotherapistJpaEntity;
import com.tfg.adapter.out.persistence.physiotherapist.RoleJpaDataRepository;
import com.tfg.adapter.out.persistence.physiotherapist.RoleJpaEntity;
import com.tfg.physiotherapist.ERole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class DataInitializer implements ApplicationRunner {

    private final RoleJpaDataRepository roleRepository;
    private final PhysiotherapistJpaDataRepository physiotherapistRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.name}")
    private String adminName;

    @Value("${admin.surname}")
    private String adminSurname;

    public DataInitializer(RoleJpaDataRepository roleRepository,
                           PhysiotherapistJpaDataRepository physiotherapistRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.physiotherapistRepository = physiotherapistRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        initRoles();
        initAdmin();
    }

    private void initRoles() {
        ERole[] roles = ERole.values();
        for (int i = 0; i < roles.length; i++) {
            ERole role = roles[i];
            roleRepository.findByName(role).orElseGet(() -> {
                int nextId = roleRepository.findAll().stream()
                        .mapToInt(RoleJpaEntity::getId).max().orElse(0) + 1;
                RoleJpaEntity entity = new RoleJpaEntity();
                entity.setId(nextId);
                entity.setName(role);
                return roleRepository.save(entity);
            });
        }
    }

    private void initAdmin() {
        boolean adminExists = physiotherapistRepository.findAll().stream()
                .anyMatch(p -> p.getRoles().stream()
                        .anyMatch(r -> r.getName() == ERole.ADMIN));

        if (adminExists) return;

        RoleJpaEntity adminRole = roleRepository.findByName(ERole.ADMIN).orElseThrow();
        RoleJpaEntity userRole = roleRepository.findByName(ERole.USER).orElseThrow();

        int nextId = physiotherapistRepository.findAll().stream()
                .mapToInt(PhysiotherapistJpaEntity::getId).max().orElse(0) + 1;

        PhysiotherapistJpaEntity admin = new PhysiotherapistJpaEntity();
        admin.setId(nextId);
        admin.setEmail(adminEmail);
        admin.setName(adminName);
        admin.setSurname(adminSurname);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRoles(Set.of(adminRole, userRole));

        physiotherapistRepository.save(admin);
    }
}
