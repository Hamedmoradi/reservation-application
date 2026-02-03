package com.azki.reservation.entity;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void noArgsConstructor_shouldCreateUser() {
        User user = new User();

        assertNotNull(user);
    }

    @Test
    void allArgsConstructor_shouldSetFieldsCorrectly() {
        User user = new User("hamed", "hamed@test.com", "H@mor1991");

        assertEquals("hamed", user.getUsername());
        assertEquals("hamed@test.com", user.getEmail());
        assertEquals("H@mor1991", user.getPassword());
    }

    @Test
    void gettersAndSetters_shouldWorkCorrectly() {
        User user = new User();

        user.setUsername("mary");
        user.setEmail("mary@test.com");
        user.setPassword("pass");

        assertEquals("mary", user.getUsername());
        assertEquals("mary@test.com", user.getEmail());
        assertEquals("pass", user.getPassword());
    }

    @Test
    void equalsAndHashCode_shouldBeEqual_whenIdsAreSame() {
        User u1 = new User();
        User u2 = new User();

        setId(u1, 1L);
        setId(u2, 1L);

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void equals_shouldNotBeEqual_whenIdsAreDifferent() {
        User u1 = new User();
        User u2 = new User();

        setId(u1, 1L);
        setId(u2, 2L);

        assertNotEquals(u1, u2);
    }


    // helper method to set id (from BaseEntity) using reflection
    private void setId(BaseEntity entity, Long id) {
        try {
            Field field = BaseEntity.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
