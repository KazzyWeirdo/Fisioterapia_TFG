package com.tfg.psychiatrist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@Getter
@Setter
public class Psychiatrist {
    private final PsychiatristId id;
    private final PsychiatristEmail email;
    private String password;
    private String name;
    private String surname;
    private Set<ERole> roles;

    public Psychiatrist(String email, String password, String name, String surname, Set<ERole> roles){
        this.id = new PsychiatristId(ThreadLocalRandom.current().nextInt(1_000_000));
        this.email = new PsychiatristEmail(email);
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.roles = roles;
    }
}
