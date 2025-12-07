package es.uab.tqs.battleship.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AttackResultTest {

    /**
     * Test Case: Verify the message associated with the HIT result.
     * * Type: Unit Testing / State Verification
     * * Technique: Equivalence Partitioning (Enum Constant).
     * * Description: Verifies that the getMessage() method correctly returns the
     * specific string ("Hit!") associated with the HIT enum constant.
     */
    @Test
    void testGetMessageHit() {
        assertEquals("Hit!", AttackResult.HIT.getMessage(), 
            "The message for HIT should be 'Hit!'");
    }

    /**
     * Test Case: Verify the message associated with the MISS result.
     * * Type: Unit Testing / State Verification
     * * Technique: Equivalence Partitioning (Enum Constant).
     * * Description: Verifies that the getMessage() method correctly returns the
     * specific string ("Water!") associated with the MISS enum constant.
     */
    @Test
    void testGetMessageMiss() {
        assertEquals("Water!", AttackResult.MISS.getMessage(), 
            "The message for MISS should be 'Water!'");
    }

    /**
     * Test Case: Verify the message associated with the SUNK result.
     * * Type: Unit Testing / State Verification
     * * Technique: Equivalence Partitioning (Enum Constant).
     * * Description: Verifies that the getMessage() method correctly returns the
     * specific string ("You sunk a ship!") associated with the SUNK enum constant.
     */
    @Test
    void testGetMessageSunk() {
        assertEquals("You sunk a ship!", AttackResult.SUNK.getMessage(), 
            "The message for SUNK should be 'You sunk a ship!'");
    }

    /**
     * Test Case: Verify the message associated with the ALREADY_ATTACKED result.
     * * Type: Unit Testing / State Verification
     * * Technique: Equivalence Partitioning (Enum Constant).
     * * Description: Verifies that the getMessage() method correctly returns the
     * specific string ("Already attacked this cell.") associated with the ALREADY_ATTACKED enum constant.
     */
    @Test
    void testGetMessageAlreadyAttacked() {
        assertEquals("Already attacked this cell.", AttackResult.ALREADY_ATTACKED.getMessage(), 
            "The message for ALREADY_ATTACKED should be 'Already attacked this cell.'");
    }
}