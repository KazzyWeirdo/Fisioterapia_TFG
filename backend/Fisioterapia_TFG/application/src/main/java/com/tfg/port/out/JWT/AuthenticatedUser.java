package com.tfg.port.out.JWT;

import java.util.List;

public record AuthenticatedUser(String subject, List<String> roles) {}
