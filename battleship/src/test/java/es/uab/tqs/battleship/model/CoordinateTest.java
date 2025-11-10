package es.uab.tqs.battleship.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests completos para la clase Coordinate
 * Técnicas aplicadas:
 * - Particiones Equivalentes (90% de particiones)
 * - Valores Límite y Frontera (90% de casos límite)
 * - Data-Driven Testing con @ParameterizedTest
 */
public class CoordinateTest {

    @Test
    public void testValidCenterCoordinate() {
        Coordinate coord = new Coordinate(5, 5);
        assertTrue(coord.isValid(10));
    }

    @Test
    public void testInvalidNegativeX() {
        Coordinate coord = new Coordinate(-1, 5);
        assertFalse(coord.isValid(10));
    }

    @Test
    public void testInvalidNegativeY() {
        Coordinate coord = new Coordinate(5, -1);
        assertFalse(coord.isValid(10));
    }

    @Test
    public void testInvalidXOutOfBounds() {
        Coordinate coord = new Coordinate(10, 5);
        assertFalse(coord.isValid(10));
    }

    @Test
    public void testInvalidYOutOfBounds() {
        Coordinate coord = new Coordinate(5, 10);
        assertFalse(coord.isValid(10));
    }

    @Test
    public void testBoundaryMinCoordinate() {
        Coordinate coord = new Coordinate(0, 0);
        assertTrue(coord.isValid(10));
    }

    @Test
    public void testBoundaryMaxCoordinate() {
        Coordinate coord = new Coordinate(9, 9);
        assertTrue(coord.isValid(10));
    }

    @Test
    public void testBoundaryInvalidMaxCoordinate() {
        Coordinate coord = new Coordinate(10, 10);
        assertFalse(coord.isValid(10));
    }

    @Test
    public void testBoundaryXMax() {
        Coordinate coord = new Coordinate(9, 5);
        assertTrue(coord.isValid(10));
    }

    @Test
    public void testBoundaryYMax() {
        Coordinate coord = new Coordinate(5, 9);
        assertTrue(coord.isValid(10));
    }

    @Test
    public void testBoundaryXJustOutOfBounds() {
        Coordinate coord = new Coordinate(10, 0);
        assertFalse(coord.isValid(10));
    }

    @Test
    public void testBoundaryYJustOutOfBounds() {
        Coordinate coord = new Coordinate(0, 10);
        assertFalse(coord.isValid(10));
    }

    @ParameterizedTest()
    @CsvSource({
        "0, 0, 10, true",
        "9, 9, 10, true",
        "5, 5, 10, true",
        "0, 9, 10, true",
        "9, 0, 10, true",
        "10, 10, 10, false",
        "-1, 5, 10, false",
        "5, -1, 10, false",
        "-1, -1, 10, false",
        "0, 10, 10, false",
        "10, 0, 10, false",
        "11, 5, 10, false",
        "5, 11, 10, false",
        "1, 1, 5, true",
        "4, 4, 5, true",
        "5, 5, 5, false"
    })
    public void testIsValidWithMultipleData(int x, int y, int boardSize, boolean expected) {
        Coordinate coord = new Coordinate(x, y);
        assertEquals(expected, coord.isValid(boardSize));
    }

    @Test
    public void testGetX() {
        Coordinate coord = new Coordinate(3, 7);
        assertEquals(3, coord.getX());
    }

    @Test
    public void testGetY() {
        Coordinate coord = new Coordinate(3, 7);
        assertEquals(7, coord.getY());
    }

    @Test
    public void testEqualsTrue() {
        Coordinate coord1 = new Coordinate(5, 5);
        Coordinate coord2 = new Coordinate(5, 5);
        assertEquals(coord1, coord2);
    }

    @Test
    public void testEqualsFalse() {
        Coordinate coord1 = new Coordinate(5, 5);
        Coordinate coord2 = new Coordinate(5, 6);
        assertNotEquals(coord1, coord2);
    }

    @Test
    public void testHashCodeConsistency() {
        Coordinate coord1 = new Coordinate(5, 5);
        Coordinate coord2 = new Coordinate(5, 5);
        assertEquals(coord1.hashCode(), coord2.hashCode());
    }

    @Test
    public void testToString() {
        Coordinate coord = new Coordinate(3, 7);
        String result = coord.toString();
        assertTrue(result.contains("3") && result.contains("7"));
    }
}

