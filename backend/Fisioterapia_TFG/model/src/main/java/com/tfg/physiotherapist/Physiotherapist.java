package com.tfg.physiotherapist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@Getter
@Setter
public class Physiotherapist {
    private final PhysiotherapistId id;
    private final PhysiotherapistEmail email;
    private String password;
    private String name;
    private String surname;
    private Set<ERole> roles;

    public Physiotherapist(String email, String password, String name, String surname, Set<ERole> roles){
        this.id = new PhysiotherapistId(ThreadLocalRandom.current().nextInt(1_000_000));
        this.email = new PhysiotherapistEmail(email);
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.roles = roles;
    }
}
