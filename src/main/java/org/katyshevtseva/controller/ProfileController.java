package org.katyshevtseva.controller;

import com.katyshevtseva.api.ProfileApi;
import com.katyshevtseva.dto.UpdateProfileRequestDto;
import com.katyshevtseva.dto.UserProfileDto;
import org.katyshevtseva.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController implements ProfileApi {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public ResponseEntity<UserProfileDto> updateProfile(
            UpdateProfileRequestDto updateProfileRequestDto
    ) {
        UserProfileDto updated = profileService.updateProfile(getUserId(), updateProfileRequestDto);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<UserProfileDto> getProfile() {
        return ResponseEntity.ok(profileService.getProfile(getUserId()));
    }

    private String getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        return jwt.getSubject();
    }
}
