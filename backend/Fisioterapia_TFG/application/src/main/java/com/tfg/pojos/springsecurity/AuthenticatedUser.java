package com.tfg.pojos.springsecurity;

import java.util.List;

public record AuthenticatedUser(String subject, List<String> roles) {}
