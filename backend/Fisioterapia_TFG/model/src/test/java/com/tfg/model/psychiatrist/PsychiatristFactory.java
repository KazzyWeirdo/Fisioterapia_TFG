package com.tfg.model.psychiatrist;

import com.tfg.psychiatrist.Psychiatrist;

public class PsychiatristFactory {
    public static String NAME = "Eduardo";
    public static String SURNAME = "Garcia";

    public static Psychiatrist createTestPsychiatrist(String email, String password) {
        Psychiatrist psychiatrist = new Psychiatrist(
                email,
                password,
                NAME,
                SURNAME
        );
        return psychiatrist;
    }
}
