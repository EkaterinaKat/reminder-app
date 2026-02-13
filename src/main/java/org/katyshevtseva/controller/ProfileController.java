package org.katyshevtseva.controller;

import com.katyshevtseva.api.ProfileApi;
import com.katyshevtseva.dto.UpdateProfileRequestDto;
import com.katyshevtseva.dto.UserProfileDto;
import org.katyshevtseva.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.katyshevtseva.util.SecurityUtil.getUserId;

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

    @Override
    public ResponseEntity<Void> deleteProfile() {
        profileService.deleteProfile(getUserId());
        return ResponseEntity.ok().build();
    }
}
