package es.uab.tqs.battleship.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BoatTest {

    @Test
    @SuppressWarnings("all")
    void testBoatConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Boat(-4,1, 3, true);
        }, "The row must be non-negative.");
        assertThrows(IllegalArgumentException.class, () -> {
            new Boat(4,-1, 3, true);
        }, "The column must be non-negative.");

        assertThrows(IllegalArgumentException.class, () -> {
            new Boat(2, 5, -1, true);
        }, "The size must be positive.");

        assertThrows(IllegalArgumentException.class, () -> {
            new Boat(5,2, 7, true);
        }, "The size must be between 1 and 5.");
    }

    @Test
    void testGetPosition() {
        Boat boat = new Boat(2, 5, 3, true);
        assertTrue(boat.getInitCol() == 5);
        assertFalse(boat.getInitCol() == 4);
        assertTrue(boat.getInitRow() == 2);
        assertFalse(boat.getInitRow() == 5);
        Boat boat2 = new Boat(7, 3, 2, true);
        assertTrue(boat2.getInitCol() == 3);
        assertFalse(boat2.getInitCol() == 7);
        assertTrue(boat2.getInitRow() == 7);
        assertFalse(boat2.getInitRow() == 5);     
    }   

    @Test
    void testGetSize() {
        Boat boat = new Boat(2, 5, 5, true);
        assertTrue(boat.getSize() == 5);
        assertFalse(boat.getSize() == 4);
        Boat boat2 = new Boat(7,3, 2, true);
        assertTrue(boat2.getSize() == 2);
        assertFalse(boat2.getSize() == 10); 
    }

    @Test
    void testIsHorizontal() {
        Boat boat = new Boat(5,1, 5, true);
        assertTrue(boat.isHorizontal());
        assertFalse(!boat.isHorizontal());
        Boat boat2 = new Boat(5, 1, 5, false);
        assertFalse(boat2.isHorizontal());
        assertTrue(!boat2.isHorizontal());
    }

    @Test
    void testGetLastPos() {
        Boat boat = new Boat(5,1, 5, true);
        assertTrue(boat.getLastPos() == 5);
        assertFalse(boat.getLastPos() == 1);
        Boat boat2 = new Boat(5, 1, 5, false);
        assertTrue(boat2.getLastPos() == 9);
        assertFalse(boat2.getLastPos() == 5);
    }  
}
