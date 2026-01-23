package org.katyshevtseva.service;

import com.katyshevtseva.dto.UpdateProfileRequestDto;
import com.katyshevtseva.dto.UserProfileDto;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    public UserProfileDto updateProfile(
            String userId,
            UpdateProfileRequestDto updateProfileRequestDto
    ) {
        return new UserProfileDto(userId);
    }

    public UserProfileDto getProfile(String userId) {
        return new UserProfileDto(userId);
    }
}
