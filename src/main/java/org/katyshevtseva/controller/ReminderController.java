package org.katyshevtseva.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ReminderController {

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/reminder/create")
    public String createReminder(Authentication auth) {
        Jwt jwt = (Jwt) auth.getPrincipal();
        return "userId " + jwt.getClaimAsString("sub") +
                "\nemail " + jwt.getClaimAsString("email") +
                "\nname " + jwt.getClaimAsString("name");
    }
}
