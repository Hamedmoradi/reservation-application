package com.azki.reservation.mapper;

import com.azki.reservation.entity.AvailableSlot;
import com.azki.reservation.model.SlotRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SlotMapper {
    SlotRequest toDto(AvailableSlot slot);

    AvailableSlot toEntity(SlotRequest slot);
}
