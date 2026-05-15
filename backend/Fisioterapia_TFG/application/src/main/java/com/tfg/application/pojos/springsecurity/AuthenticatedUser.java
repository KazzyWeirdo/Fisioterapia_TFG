package com.tfg.application.pojos.springsecurity;

import java.util.List;

public record AuthenticatedUser(String subject, List<String> roles, String name, String surname) {}
