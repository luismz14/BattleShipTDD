package es.uab.tqs.battleship.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShipTypeTest {

    /**
     * Test Case: Verify the display names for UI representation.
     * * Type: Unit Testing
     * * Technique: Statement Coverage / Equivalence Partitioning (Enum Constants).
     * * Description: Verifies that each ShipType enum constant returns the expected human-readable
     * string via getDisplayName(). This is crucial for the View layer to display correct names.
     */
    @Test
    void testGetDisplayName() {
        assertEquals("Carrier", ShipType.CARRIER.getDisplayName());
        assertEquals("Battleship", ShipType.BATTLESHIP.getDisplayName());
        assertEquals("Cruiser", ShipType.CRUISER.getDisplayName());
        assertEquals("Submarine", ShipType.SUBMARINE.getDisplayName());
        assertEquals("Destroyer", ShipType.DESTROYER.getDisplayName());
    }

    /**
     * Test Case: Verify the length configuration for each ship type.
     * * Type: Unit Testing / State Verification
     * * Technique: Statement Coverage / Boundary Value Analysis (Implicit definitions).
     * * Description: Verifies that each ShipType is configured with the correct length value according
     * to the game rules (e.g., Carrier = 5, Destroyer = 2). This ensures the game logic uses
     * the correct dimensions for placement and collision detection.
     */
    @Test
    void testGetLength() {
        assertEquals(5, ShipType.CARRIER.getLength());
        assertEquals(4, ShipType.BATTLESHIP.getLength());
        assertEquals(3, ShipType.CRUISER.getLength());
        assertEquals(3, ShipType.SUBMARINE.getLength());
        assertEquals(2, ShipType.DESTROYER.getLength());
    }
}
