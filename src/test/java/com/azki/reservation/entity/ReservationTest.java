package com.azki.reservation.entity;

import com.azki.reservation.exception.SlotAlreadyReservedException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @Test
    void constructor_shouldCreateReservation_whenSlotIsNotReserved() {
        // given
        AvailableSlot slot = new AvailableSlot();
        slot.setReserved(false);

        User user = new User();
        user.setUsername("hamed");
        user.setEmail("hamed@test.com");
        user.setPassword("H@mor1991");

        // when
        Reservation reservation = new Reservation(slot, user);

        // then
        assertThat(reservation.getAvailableSlot()).isEqualTo(slot);
        assertThat(reservation.getUser()).isEqualTo(user);
    }

    @Test
    void constructor_shouldThrowException_whenSlotIsReserved() {
        // given
        AvailableSlot slot = new AvailableSlot();
        slot.setReserved(true);

        User user = new User();
        user.setUsername("hamed");
        user.setEmail("hamed@test.com");
        user.setPassword("H@mor1991");

        // then
        assertThatThrownBy(() -> new Reservation(slot, user))
                .isInstanceOf(SlotAlreadyReservedException.class)
                .hasMessage("slot already reserved.");
    }

    @Test
    void prePersist_shouldSetCreatedAt() {
        // given
        Reservation reservation = new Reservation();

        reservation.prePersist();

        // then
        assertThat(reservation.getCreatedAt()).isNotNull();
    }

    @Test
    void equalsAndHashCode_shouldBeEqual_whenIdsAreSame() {
        Reservation r1 = new Reservation();
        Reservation r2 = new Reservation();

        setId(r1, 1L);
        setId(r2, 1L);

        assertThat(r1).isEqualTo(r2);
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }

    @Test
    void equalsAndHashCode_shouldNotBeEqual_whenIdsAreDifferent() {
        Reservation r1 = new Reservation();
        Reservation r2 = new Reservation();

        setId(r1, 1L);
        setId(r2, 2L);

        assertThat(r1).isNotEqualTo(r2);
    }

    @Test
    void gettersAndSetters_shouldWork() {
        Reservation reservation = new Reservation();

        AvailableSlot slot = new AvailableSlot();
        User user = new User();

        reservation.setAvailableSlot(slot);
        reservation.setUser(user);

        assertThat(reservation.getAvailableSlot()).isEqualTo(slot);
        assertThat(reservation.getUser()).isEqualTo(user);
    }

    // helper to set private id field
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
