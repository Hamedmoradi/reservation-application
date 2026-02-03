package com.azki.reservation.entity;

import com.azki.reservation.entity.AvailableSlot;
import com.azki.reservation.entity.BaseEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AvailableSlotTest {

    @Test
    void shouldCreateAvailableSlotSuccessfully() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);

        AvailableSlot slot = new AvailableSlot();
        slot.setStartTime(start);
        slot.setEndTime(end);
        slot.setReserved(false);

        assertThat(slot.getStartTime()).isEqualTo(start);
        assertThat(slot.getEndTime()).isEqualTo(end);
        assertThat(slot.isReserved()).isFalse();
    }

    @Test
    void prePersist_shouldSetCreatedAt() {
        AvailableSlot slot = new AvailableSlot();
        slot.prePersist();
        assertThat(slot.getCreatedAt()).isNotNull();
    }

    @Test
    void equalsAndHashCode_shouldWorkBasedOnId() {
        AvailableSlot slot1 = new AvailableSlot();
        AvailableSlot slot2 = new AvailableSlot();

        setId(slot1, 1L);
        setId(slot2, 1L);

        assertThat(slot1).isEqualTo(slot2);
        assertThat(slot1.hashCode()).isEqualTo(slot2.hashCode());
    }

    @Test
    void toString_shouldContainFields() {
        AvailableSlot slot = new AvailableSlot();
        slot.setStartTime(LocalDateTime.now());
        slot.setEndTime(LocalDateTime.now().plusHours(1));
        slot.setReserved(true);

        String text = slot.toString();

        assertThat(text).contains("AvailableSlot");
    }

    private void setId(BaseEntity entity, Long id) {
        try {
            var field = BaseEntity.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
