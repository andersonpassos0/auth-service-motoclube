package com.motoclub.authservice.application.dto;

import java.util.List;

public record AuthUserResponse(String username, List<String> roles) {}

