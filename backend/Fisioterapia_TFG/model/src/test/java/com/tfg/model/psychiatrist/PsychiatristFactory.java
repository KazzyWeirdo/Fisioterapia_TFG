package com.tfg.model.psychiatrist;

import com.tfg.psychiatrist.Psychiatrist;
import com.tfg.psychiatrist.Roles;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PsychiatristFactory {
    public static String NAME = "Eduardo";
    public static String SURNAME = "Garcia";
    public static Set<Roles> ROLES = new HashSet<>(Arrays.asList(Roles.ADMIN, Roles.USER));

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
