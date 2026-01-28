package org.katyshevtseva.mapper;

import com.katyshevtseva.dto.ReminderDto;
import com.katyshevtseva.dto.ReminderRequestDto;
import org.katyshevtseva.entity.Reminder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReminderMapper {

    Reminder toEntity(ReminderRequestDto dto);

    ReminderDto toDto(Reminder entity);

    void updateReminderFromDto(ReminderRequestDto dto, @MappingTarget Reminder reminder);
}
