package com.tfg.model.physiotherapist;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PhysiotherapistFactory {
    public static String NAME = "Eduardo";
    public static String SURNAME = "Garcia";
    public static Set<ERole> ROLES = new HashSet<>(Arrays.asList(ERole.ADMIN, ERole.USER));

    public static Physiotherapist createTestPsychiatrist(String email, String password) {
        Physiotherapist psychiatrist = new Physiotherapist(
                email,
                password,
                NAME,
                SURNAME,
                ROLES
        );
        return psychiatrist;
    }
}
