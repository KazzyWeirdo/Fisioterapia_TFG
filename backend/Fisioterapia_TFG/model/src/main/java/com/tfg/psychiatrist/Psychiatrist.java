package com.tfg.psychiatrist;

import com.tfg.patient.PatientId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@Getter
@Setter
public class Psychiatrist {
    private final PsychiatristId id;
    private final PsychiatristEmail email;
    private final PsychiatristPassword password;
    private String name;
    private String surname;

    public Psychiatrist(String email, String password, String name, String surname){
        this.id = new PsychiatristId(ThreadLocalRandom.current().nextInt(1_000_000));
        this.email = new PsychiatristEmail(email);
        this.password = new PsychiatristPassword(password);
        this.name = name;
        this.surname = surname;

    }
}
