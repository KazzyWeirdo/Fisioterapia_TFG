package com.tfg.psychiatrist;

import com.tfg.patient.PatientId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@Getter
@Setter
public class Psychiatrist {
    private final PsychiatristId id;
    private final PsychiatristEmail email;
    private final String password;
    private String name;
    private String surname;
    private Set<Roles> roles;

    public Psychiatrist(String email, String password, String name, String surname, Set<Roles> roles){
        this.id = new PsychiatristId(ThreadLocalRandom.current().nextInt(1_000_000));
        this.email = new PsychiatristEmail(email);
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.roles = roles;
    }

    public boolean passwordIsCorrect(String password) {
        try {
            PsychiatristPassword psychiatristPassword = new PsychiatristPassword(this.password);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
