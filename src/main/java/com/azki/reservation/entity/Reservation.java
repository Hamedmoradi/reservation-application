package com.azki.reservation.entity;

import com.azki.reservation.enums.AppExceptionStatusEnum;
import com.azki.reservation.exception.SlotAlreadyReservedException;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservations")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class Reservation extends BaseEntity {

    @OneToOne(optional = false, cascade = CascadeType.MERGE)
    private AvailableSlot availableSlot;

    @ManyToOne(optional = false)
    private User user;

    public Reservation(@NonNull AvailableSlot availableSlot, @NonNull User user) {

        if (availableSlot.isReserved()) {
            throw new SlotAlreadyReservedException(AppExceptionStatusEnum.SLOT_ALREADY_RESERVED.getStatus(),
                    AppExceptionStatusEnum.SLOT_ALREADY_RESERVED.getMessage());
        }
        this.availableSlot = availableSlot;
        this.user = user;
    }
}
