package org.katyshevtseva.repository;

import org.katyshevtseva.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {

    default UserProfile getOrCreate(String id) {
        return findById(id).orElseGet(() -> {
            UserProfile user = new UserProfile();
            user.setId(id);
            return save(user);
        });
    }
}
