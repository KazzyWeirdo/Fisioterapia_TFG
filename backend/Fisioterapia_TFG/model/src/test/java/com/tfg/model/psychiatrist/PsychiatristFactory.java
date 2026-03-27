package com.tfg.model.psychiatrist;

import com.tfg.psychiatrist.Psychiatrist;
import com.tfg.psychiatrist.ERole;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PsychiatristFactory {
    public static String NAME = "Eduardo";
    public static String SURNAME = "Garcia";
    public static Set<ERole> ROLES = new HashSet<>(Arrays.asList(ERole.ADMIN, ERole.USER));

    public static Psychiatrist createTestPsychiatrist(String email, String password) {
        Psychiatrist psychiatrist = new Psychiatrist(
                email,
                password,
                NAME,
                SURNAME,
                ROLES
        );
        return psychiatrist;
    }
}
