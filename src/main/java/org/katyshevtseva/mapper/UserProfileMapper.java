package org.katyshevtseva.mapper;

import com.katyshevtseva.dto.UserProfileDto;
import org.katyshevtseva.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {

    @Mapping(source = "id", target = "userId")
    UserProfileDto toDto(UserProfile profile);
}
