package org.katyshevtseva.service;

import com.katyshevtseva.dto.UpdateProfileRequestDto;
import com.katyshevtseva.dto.UserProfileDto;
import org.katyshevtseva.entity.UserProfile;
import org.katyshevtseva.mapper.UserProfileMapper;
import org.katyshevtseva.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProfileService {

    private final UserProfileRepository repository;
    private final UserProfileMapper mapper;

    public ProfileService(UserProfileRepository repository, UserProfileMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public UserProfileDto updateProfile(String userId, UpdateProfileRequestDto requestDto) {
        UserProfile profile = getOrCreateProfile(userId);
        profile.setEmail(requestDto.getEmail());
        profile.setTelegram(requestDto.getTelegram());
        return mapper.toDto(repository.save(profile));
    }

    public UserProfileDto getProfile(String userId) {
        return mapper.toDto(getOrCreateProfile(userId));
    }

    public void deleteProfile(String userId) {
        repository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    private UserProfile getOrCreateProfile(String userId) {
        Optional<UserProfile> existing = repository.findById(userId);
        if (existing.isPresent()) {
            return existing.get();
        }
        UserProfile newProfile = new UserProfile();
        newProfile.setId(userId);
        return newProfile;
    }
}
