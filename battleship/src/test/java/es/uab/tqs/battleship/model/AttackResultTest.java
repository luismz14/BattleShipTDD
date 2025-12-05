package es.uab.tqs.battleship.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AttackResultTest {
    @Test
    void testGetMessageHit() {
        assertEquals("Hit!", AttackResult.HIT.getMessage(), 
            "El mensaje para HIT debería ser 'Hit!'");
    }

    @Test
    void testGetMessageMiss() {
        assertEquals("Water!", AttackResult.MISS.getMessage(), 
            "El mensaje para MISS debería ser 'Water!'");
    }

    @Test
    void testGetMessageSunk() {
        assertEquals("You sunk a ship!", AttackResult.SUNK.getMessage(), 
            "El mensaje para SUNK debería ser 'You sunk a ship!'");
    }

    @Test
    void testGetMessageAlreadyAttacked() {
        assertEquals("Already attacked this cell.", AttackResult.ALREADY_ATTACKED.getMessage(), 
            "El mensaje para ALREADY_ATTACKED debería ser 'Already attacked this cell.'");
    }
}
