package es.uab.tqs.battleship.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShipTypeTest {
    @Test
    void testGetDisplayName() {
        assertEquals("Carrier", ShipType.CARRIER.getDisplayName());
        assertEquals("Battleship", ShipType.BATTLESHIP.getDisplayName());
        assertEquals("Cruiser", ShipType.CRUISER.getDisplayName());
        assertEquals("Submarine", ShipType.SUBMARINE.getDisplayName());
        assertEquals("Destroyer", ShipType.DESTROYER.getDisplayName());
    }

    @Test
    void testGetLength() {
        assertEquals(5, ShipType.CARRIER.getLength());
        assertEquals(4, ShipType.BATTLESHIP.getLength());
        assertEquals(3, ShipType.CRUISER.getLength());
        assertEquals(3, ShipType.SUBMARINE.getLength());
        assertEquals(2, ShipType.DESTROYER.getLength());
    }
}
