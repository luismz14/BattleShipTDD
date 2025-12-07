package es.uab.tqs.battleship.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

// Test class for Coordinate: unit tests, limit values, boundary values and pairwise testing
public class CoordinateTest {

    /**
     * Test Case: Verify a coordinate well within the board limits.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Valid Class.
     * * Description: Verifies that a coordinate located in the middle of the board (5,5)
     * returns true for isValid(), representing the standard safe case.
     */
    @Test
    public void testValidCenterCoordinate() {
        Coordinate coord = new Coordinate(5, 5);
        assertTrue(coord.isValid(10));
    }

    /**
     * Test Case: Verify validation for negative X coordinate.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis (Invalid Lower Bound).
     * * Description: Verifies that a negative X value (-1) is correctly identified as invalid,
     * protecting against underflow errors.
     */
    @Test
    public void testInvalidNegativeX() {
        Coordinate coord = new Coordinate(-1, 5);
        assertFalse(coord.isValid(10));
    }

    /**
     * Test Case: Verify validation for negative Y coordinate.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis (Invalid Lower Bound).
     * * Description: Verifies that a negative Y value (-1) is correctly identified as invalid.
     */
    @Test
    public void testInvalidNegativeY() {
        Coordinate coord = new Coordinate(5, -1);
        assertFalse(coord.isValid(10));
    }

    /**
     * Test Case: Verify validation for X coordinate exceeding board size.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis (Invalid Upper Bound).
     * * Description: Verifies that an X value equal to the board size (10) is invalid
     * (since indices are 0-based, 10 is out of bounds).
     */
    @Test
    public void testInvalidXOutOfBounds() {
        Coordinate coord = new Coordinate(10, 5);
        assertFalse(coord.isValid(10));
    }

    /**
     * Test Case: Verify validation for Y coordinate exceeding board size.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis (Invalid Upper Bound).
     * * Description: Verifies that a Y value equal to the board size (10) is invalid.
     */
    @Test
    public void testInvalidYOutOfBounds() {
        Coordinate coord = new Coordinate(5, 10);
        assertFalse(coord.isValid(10));
    }

    /**
     * Test Case: Verify the minimum valid coordinate.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis (Valid Lower Bound).
     * * Description: Verifies that (0,0) is accepted as a valid coordinate, ensuring the
     * lower limit is inclusive.
     */
    @Test
    public void testBoundaryMinCoordinate() {
        Coordinate coord = new Coordinate(0, 0);
        assertTrue(coord.isValid(10));
    }

    /**
     * Test Case: Verify the maximum valid coordinate.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis (Valid Upper Bound).
     * * Description: Verifies that (9,9) is accepted for a board of size 10, ensuring the
     * upper limit is handled correctly (size - 1).
     */
    @Test
    public void testBoundaryMaxCoordinate() {
        Coordinate coord = new Coordinate(9, 9);
        assertTrue(coord.isValid(10));
    }

    /**
     * Test Case: Verify the first invalid coordinate.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis (Invalid Upper Bound).
     * * Description: Verifies that (10,10) is rejected, confirming the boundary is strict.
     */
    @Test
    public void testBoundaryInvalidMaxCoordinate() {
        Coordinate coord = new Coordinate(10, 10);
        assertFalse(coord.isValid(10));
    }

    /**
     * Test Case: Verify the maximum valid X coordinate.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis.
     * * Description: Checks the exact edge case for X (9) while keeping Y safe.
     */
    @Test
    public void testBoundaryXMax() {
        Coordinate coord = new Coordinate(9, 5);
        assertTrue(coord.isValid(10));
    }

    /**
     * Test Case: Verify the maximum valid Y coordinate.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis.
     * * Description: Checks the exact edge case for Y (9) while keeping X safe.
     */
    @Test
    public void testBoundaryYMax() {
        Coordinate coord = new Coordinate(5, 9);
        assertTrue(coord.isValid(10));
    }

    /**
     * Test Case: Verify X just outside boundary.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis.
     * * Description: Checks that 10 (size) is rejected for X.
     */
    @Test
    public void testBoundaryXJustOutOfBounds() {
        Coordinate coord = new Coordinate(10, 0);
        assertFalse(coord.isValid(10));
    }

    /**
     * Test Case: Verify Y just outside boundary.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis.
     * * Description: Checks that 10 (size) is rejected for Y.
     */
    @Test
    public void testBoundaryYJustOutOfBounds() {
        Coordinate coord = new Coordinate(0, 10);
        assertFalse(coord.isValid(10));
    }

    /**
     * Test Case: Data-Driven validation of coordinates (Multiple Scenarios).
     * * Type: Black Box Testing / Data Driven Testing
     * * Technique: Parameterized Testing (CsvSource).
     * * Description: Automatically executes the same test logic against a wide range of
     * inputs defined in a CSV format. This covers valid cases, boundary values, negatives,
     * and varying board sizes in a single method, maximizing test coverage efficiency.
     * * Inputs: X, Y, BoardSize, ExpectedResult.
     */
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

    /**
     * Test Case: Verify X coordinate retrieval.
     * * Type: Unit Testing
     * * Technique: Statement Coverage.
     */
    @Test
    public void testGetX() {
        Coordinate coord = new Coordinate(3, 7);
        assertEquals(3, coord.getX());
    }

    /**
     * Test Case: Verify Y coordinate retrieval.
     * * Type: Unit Testing
     * * Technique: Statement Coverage.
     */
    @Test
    public void testGetY() {
        Coordinate coord = new Coordinate(3, 7);
        assertEquals(7, coord.getY());
    }

    /**
     * Test Case: Verify equality contract.
     * * Type: Unit Testing
     * * Technique: Statement Coverage (Equals contract).
     * * Description: Verifies that two different objects with the same X and Y values
     * are considered equal, which is essential for Value Objects like Coordinate.
     */
    @Test
    public void testEqualsTrue() {
        Coordinate coord1 = new Coordinate(5, 5);
        Coordinate coord2 = new Coordinate(5, 5);
        assertEquals(coord1, coord2);
    }

    /**
     * Test Case: Verify equality contract.
     * * Type: Unit Testing
     * * Technique: Statement Coverage.
     * * Description: Verifies that coordinates with different values are not considered equal.
     */
    @Test
    public void testEqualsFalse() {
        Coordinate coord1 = new Coordinate(5, 5);
        Coordinate coord2 = new Coordinate(5, 6);
        assertNotEquals(coord1, coord2);
    }

    /**
     * Test Case: Verify HashCode consistency.
     * * Type: Unit Testing
     * * Technique: Statement Coverage (HashCode contract).
     * * Description: Verifies that if two objects are equal, their hash codes must also be equal.
     * This is critical for using Coordinates in HashMaps or HashSets.
     */
    @Test
    public void testHashCodeConsistency() {
        Coordinate coord1 = new Coordinate(5, 5);
        Coordinate coord2 = new Coordinate(5, 5);
        assertEquals(coord1.hashCode(), coord2.hashCode());
    }

    /**
     * Test Case: Verify String representation.
     * * Type: Unit Testing
     * * Technique: Statement Coverage.
     * * Description: Ensures the toString method returns a readable string containing the
     * coordinate values, useful for debugging and logging.
     */
    @Test
    public void testToString() {
        Coordinate coord = new Coordinate(3, 7);
        String result = coord.toString();
        assertTrue(result.contains("3") && result.contains("7"));
    }
}

