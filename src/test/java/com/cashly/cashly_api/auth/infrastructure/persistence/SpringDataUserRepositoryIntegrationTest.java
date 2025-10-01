package com.cashly.cashly_api.auth.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("SpringDataUserRepository Integration Tests")
class SpringDataUserRepositoryIntegrationTest {

    @Autowired
    private SpringDataUserRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(UUID.randomUUID().toString());
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash("$2a$10$hashedPassword");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setActive(true);
        testUser.setEmailVerified(false);
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("should_SaveUser_When_ValidUserProvided")
    void should_SaveUser_When_ValidUserProvided() {
        UserEntity savedUser = repository.save(testUser);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(testUser.getEmail(), savedUser.getEmail());
        assertEquals(testUser.getFirstName(), savedUser.getFirstName());
    }

    @Test
    @DisplayName("should_FindUserById_When_UserExists")
    void should_FindUserById_When_UserExists() {
        entityManager.persistAndFlush(testUser);

        Optional<UserEntity> foundUser = repository.findById(testUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getEmail(), foundUser.get().getEmail());
    }

    @Test
    @DisplayName("should_ReturnEmpty_When_UserIdNotExists")
    void should_ReturnEmpty_When_UserIdNotExists() {
        Optional<UserEntity> foundUser = repository.findById(UUID.randomUUID().toString());

        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("should_FindUserByEmail_When_EmailExists")
    void should_FindUserByEmail_When_EmailExists() {
        entityManager.persistAndFlush(testUser);

        Optional<UserEntity> foundUser = repository.findByEmail(testUser.getEmail());

        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getId(), foundUser.get().getId());
        assertEquals(testUser.getEmail(), foundUser.get().getEmail());
    }

    @Test
    @DisplayName("should_ReturnEmpty_When_EmailNotExists")
    void should_ReturnEmpty_When_EmailNotExists() {
        Optional<UserEntity> foundUser = repository.findByEmail("nonexistent@example.com");

        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("should_ReturnTrue_When_EmailExists")
    void should_ReturnTrue_When_EmailExists() {
        entityManager.persistAndFlush(testUser);

        boolean exists = repository.existsByEmail(testUser.getEmail());

        assertTrue(exists);
    }

    @Test
    @DisplayName("should_ReturnFalse_When_EmailNotExists")
    void should_ReturnFalse_When_EmailNotExists() {
        boolean exists = repository.existsByEmail("nonexistent@example.com");

        assertFalse(exists);
    }

    @Test
    @DisplayName("should_UpdateUser_When_UserModified")
    void should_UpdateUser_When_UserModified() {
        entityManager.persistAndFlush(testUser);

        testUser.setFirstName("Jane");
        testUser.setUpdatedAt(LocalDateTime.now());
        UserEntity updatedUser = repository.save(testUser);

        assertEquals("Jane", updatedUser.getFirstName());
    }

    @Test
    @DisplayName("should_DeleteUser_When_UserExists")
    void should_DeleteUser_When_UserExists() {
        entityManager.persistAndFlush(testUser);

        repository.deleteById(testUser.getId());
        Optional<UserEntity> foundUser = repository.findById(testUser.getId());

        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("should_FindAllUsers_When_MultipleUsersExist")
    void should_FindAllUsers_When_MultipleUsersExist() {
        UserEntity user2 = new UserEntity();
        user2.setId(UUID.randomUUID().toString());
        user2.setEmail("user2@example.com");
        user2.setPasswordHash("$2a$10$hashedPassword2");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setActive(true);
        user2.setEmailVerified(false);
        user2.setCreatedAt(LocalDateTime.now());
        user2.setUpdatedAt(LocalDateTime.now());

        entityManager.persistAndFlush(testUser);
        entityManager.persistAndFlush(user2);

        var users = repository.findAll();

        assertTrue(users.size() >= 2);
    }

    @Test
    @DisplayName("should_PreventDuplicateEmails_When_SavingUser")
    void should_PreventDuplicateEmails_When_SavingUser() {
        entityManager.persistAndFlush(testUser);

        UserEntity duplicateUser = new UserEntity();
        duplicateUser.setId(UUID.randomUUID().toString());
        duplicateUser.setEmail(testUser.getEmail());
        duplicateUser.setPasswordHash("$2a$10$hashedPassword2");
        duplicateUser.setFirstName("Jane");
        duplicateUser.setLastName("Smith");
        duplicateUser.setActive(true);
        duplicateUser.setEmailVerified(false);
        duplicateUser.setCreatedAt(LocalDateTime.now());
        duplicateUser.setUpdatedAt(LocalDateTime.now());

        assertThrows(Exception.class, () -> {
            repository.saveAndFlush(duplicateUser);
        });
    }
}
