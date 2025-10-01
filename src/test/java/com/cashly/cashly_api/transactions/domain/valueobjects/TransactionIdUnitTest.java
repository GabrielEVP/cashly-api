package com.cashly.cashly_api.transactions.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionIdUnitTest {

    @Test
    void should_CreateTransactionId_When_ValidUUIDProvided() {
        // Arrange
        UUID uuid = UUID.randomUUID();

        // Act
        TransactionId transactionId = new TransactionId(uuid);

        // Assert
        assertNotNull(transactionId);
        assertEquals(uuid, transactionId.getValue());
    }

    @Test
    void should_ThrowException_When_NullUUIDProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new TransactionId(null)
        );
        assertEquals("Transaction ID cannot be null", exception.getMessage());
    }

    @Test
    void should_GenerateUniqueTransactionId_When_GenerateCalled() {
        // Act
        TransactionId id1 = TransactionId.generate();
        TransactionId id2 = TransactionId.generate();

        // Assert
        assertNotNull(id1);
        assertNotNull(id2);
        assertNotEquals(id1, id2);
    }

    @Test
    void should_CreateTransactionId_When_ValidStringProvided() {
        // Arrange
        String uuidString = UUID.randomUUID().toString();

        // Act
        TransactionId transactionId = TransactionId.from(uuidString);

        // Assert
        assertNotNull(transactionId);
        assertEquals(uuidString, transactionId.getValue().toString());
    }

    @Test
    void should_ThrowException_When_NullStringProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TransactionId.from(null)
        );
        assertEquals("Transaction ID string cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_InvalidStringFormatProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TransactionId.from("invalid-uuid")
        );
        assertEquals("Invalid transaction ID format", exception.getMessage());
    }

    @Test
    void should_TrimWhitespace_When_StringWithSpacesProvided() {
        // Arrange
        String uuidString = UUID.randomUUID().toString();
        String uuidWithSpaces = "  " + uuidString + "  ";

        // Act
        TransactionId transactionId = TransactionId.from(uuidWithSpaces);

        // Assert
        assertEquals(uuidString, transactionId.getValue().toString());
    }

    @Test
    void should_BeEqual_When_SameUUIDUsed() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        TransactionId id1 = new TransactionId(uuid);
        TransactionId id2 = new TransactionId(uuid);

        // Act & Assert
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentUUIDUsed() {
        // Arrange
        TransactionId id1 = TransactionId.generate();
        TransactionId id2 = TransactionId.generate();

        // Act & Assert
        assertNotEquals(id1, id2);
    }

    @Test
    void should_ReturnTrueForEquals_When_SameInstance() {
        // Arrange
        TransactionId id = TransactionId.generate();

        // Act & Assert
        assertEquals(id, id);
    }

    @Test
    void should_ReturnFalseForEquals_When_NullProvided() {
        // Arrange
        TransactionId id = TransactionId.generate();

        // Act & Assert
        assertNotEquals(null, id);
    }

    @Test
    void should_ReturnFalseForEquals_When_DifferentClassProvided() {
        // Arrange
        TransactionId id = TransactionId.generate();
        String other = "not a transaction id";

        // Act & Assert
        assertNotEquals(id, other);
    }

    @Test
    void should_ReturnStringRepresentation_When_ToStringCalled() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        TransactionId transactionId = new TransactionId(uuid);

        // Act
        String result = transactionId.toString();

        // Assert
        assertTrue(result.contains("TransactionId"));
        assertTrue(result.contains(uuid.toString()));
    }
}
