package es.uab.tqs.battleship.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

public class ShipTest {

    /**
     * Test Case: Verify ship lengths for all ship types.
     * * Type: Black Box Testing / Data Driven Testing
     * * Technique: Equivalence Partitioning (Enum Source) / Parameterized Test.
     * * Description: Automatically iterates through every constant in the ShipType enum,
     * verifying that the Ship constructor correctly sets the length associated with that type.
     * This ensures the model is consistent across all possible ship variants.
     */
    @ParameterizedTest
    @EnumSource(ShipType.class)
    public void testShipTypeLength(ShipType type) {
        Ship ship = new Ship(type);
        assertEquals(type.getLength(), ship.getLength());
    }

    /**
     * Test Case: Verify length of the smallest ship (Lower Boundary).
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis.
     * * Description: Verifies the length of the DESTROYER, which represents the minimum
     * valid length (2) for a ship in this game ruleset.
     */
    @Test
    public void testMinShipLength() {
        Ship ship = new Ship(ShipType.DESTROYER);
        assertEquals(2, ship.getLength());
    }

    /**
     * Test Case: Verify length of the largest ship (Upper Boundary).
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis.
     * * Description: Verifies the length of the CARRIER, which represents the maximum
     * valid length (5) for a ship in this game ruleset.
     */
    @Test
    public void testMaxShipLength() {
        Ship ship = new Ship(ShipType.CARRIER);
        assertEquals(5, ship.getLength());
    }

    /**
     * Test Case: Verify ship positioning with combinatorial inputs.
     * * Type: Black Box Testing / Data Driven Testing
     * * Technique: Pairwise Testing / Parameterized Test.
     * * Description: Tests multiple combinations of coordinates (X, Y), orientations,
     * and ship types to verify that 'setPosition' correctly calculates the ship's footprint.
     * Using CsvSource allows covering diverse scenarios (corner cases, mixed orientations) efficiently.
     * * Inputs: X, Y, Orientation, ShipType.
     */
    @ParameterizedTest()
    @CsvSource({
        "0, 0, HORIZONTAL, CARRIER",
        "0, 0, VERTICAL, DESTROYER",
        "5, 5, HORIZONTAL, DESTROYER",
        "5, 5, VERTICAL, CARRIER",
        "9, 0, HORIZONTAL, CRUISER",
        "0, 9, VERTICAL, SUBMARINE",
        "9, 9, HORIZONTAL, BATTLESHIP",
        "1, 1, VERTICAL, BATTLESHIP",
        "2, 3, HORIZONTAL, SUBMARINE",
        "3, 2, VERTICAL, CRUISER"
    })
    public void testSetPositionPairwise(int x, int y, Orientation orientation, ShipType type) {
        Ship ship = new Ship(type);
        Coordinate start = new Coordinate(x, y);

        ship.setPosition(start, orientation);

        assertEquals(orientation, ship.getOrientation());
        assertEquals(type.getLength(), ship.getCoordinates().size());
        assertEquals(start, ship.getCoordinates().get(0));
    }

    /**
     * Test Case: Verify coordinate calculation for Horizontal orientation.
     * * Type: White Box Testing
     * * Technique: Statement Coverage.
     * * Description: Verifies that setting a ship horizontally generates a list of coordinates
     * where X increments (2, 3, 4) and Y remains constant (5).
     */
    @Test
    public void testHorizontalShipCoordinates() {
        Ship ship = new Ship(ShipType.CRUISER);
        ship.setPosition(new Coordinate(2, 5), Orientation.HORIZONTAL);

        var coords = ship.getCoordinates();
        assertEquals(3, coords.size());
        assertEquals(new Coordinate(2, 5), coords.get(0));
        assertEquals(new Coordinate(3, 5), coords.get(1));
        assertEquals(new Coordinate(4, 5), coords.get(2));
    }

    /**
     * Test Case: Verify coordinate calculation for Vertical orientation.
     * * Type: White Box Testing
     * * Technique: Statement Coverage.
     * * Description: Verifies that setting a ship vertically generates a list of coordinates
     * where X remains constant (5) and Y increments (2, 3, 4).
     */
    @Test
    public void testVerticalShipCoordinates() {
        Ship ship = new Ship(ShipType.SUBMARINE);
        ship.setPosition(new Coordinate(5, 2), Orientation.VERTICAL);

        var coords = ship.getCoordinates();
        assertEquals(3, coords.size());
        assertEquals(new Coordinate(5, 2), coords.get(0));
        assertEquals(new Coordinate(5, 3), coords.get(1));
        assertEquals(new Coordinate(5, 4), coords.get(2));
    }

    /**
     * Test Case: Verify 'isSunk' logic under various damage conditions.
     * * Type: Black Box Testing / Data Driven Testing
     * * Technique: Boundary Value Analysis (Hits vs Length) / Parameterized Test.
     * * Description: Verifies whether a ship is considered sunk based on its type (Length)
     * and the number of hits received.
     * - Case 1: Hits < Length -> Not Sunk.
     * - Case 2: Hits == Length -> Sunk.
     * * Inputs: ShipType, Hits, ExpectedResult.
     */
    @ParameterizedTest()
    @CsvSource({
        "DESTROYER, 0, false",
        "DESTROYER, 1, false",
        "DESTROYER, 2, true",
        "CRUISER, 0, false",
        "CRUISER, 2, false",
        "CRUISER, 3, true",
        "CARRIER, 0, false",
        "CARRIER, 3, false",
        "CARRIER, 5, true",
        "BATTLESHIP, 4, true",
        "SUBMARINE, 3, true"
    })
    public void testIsSunkDataDriven(ShipType type, int hits, boolean expectedSunk) {
        Ship ship = new Ship(type);

        for (int i = 0; i < hits; i++) {
            ship.registerHit();
        }

        assertEquals(expectedSunk, ship.isSunk());
    }

    /**
     * Test Case: Verify initial hit count.
     * * Type: Unit Testing
     * * Technique: Statement Coverage.
     * * Description: Verifies that a new ship starts with 0 damage.
     */
    @Test
    public void testInitialHitCountIsZero() {
        Ship ship = new Ship(ShipType.CARRIER);
        assertEquals(0, ship.getHitCount());
        assertFalse(ship.isSunk());
    }

    /**
     * Test Case: Verify hit accumulation.
     * * Type: White Box Testing
     * * Technique: State Transition Testing (Healthy -> Damaged).
     * * Description: Verifies that calling registerHit() correctly increments the
     * internal hit counter without sinking the ship if the limit isn't reached.
     */
    @Test
    public void testPartiallyDamagedShip() {
        Ship ship = new Ship(ShipType.BATTLESHIP);

        ship.registerHit();
        assertEquals(1, ship.getHitCount());
        assertFalse(ship.isSunk());

        ship.registerHit();
        assertEquals(2, ship.getHitCount());
        assertFalse(ship.isSunk());
    }

    /**
     * Test Case: Verify transition to Sunk state at exact damage limit.
     * * Type: Black Box Testing
     * * Technique: Boundary Value Analysis (Limit Value).
     * * Description: Verifies that the ship remains afloat at (Length - 1) hits
     * and strictly transitions to Sunk at (Length) hits.
     */
    @Test
    public void testShipSunkAtExactLimit() {
        Ship ship = new Ship(ShipType.DESTROYER);

        ship.registerHit();
        assertFalse(ship.isSunk());

        ship.registerHit();
        assertTrue(ship.isSunk());
    }

    /**
     * Test Case: Verify coordinate membership check.
     * * Type: White Box Testing
     * * Technique: Statement Coverage / List Search.
     * * Description: Verifies that occupiesCoordinate returns true for all coordinates
     * calculated to be part of the ship's position.
     */
    @Test
    public void testOccupiesCoordinateFound() {
        Ship ship = new Ship(ShipType.CRUISER);
        ship.setPosition(new Coordinate(3, 3), Orientation.HORIZONTAL);

        assertTrue(ship.occupiesCoordinate(new Coordinate(3, 3)));
        assertTrue(ship.occupiesCoordinate(new Coordinate(4, 3)));
        assertTrue(ship.occupiesCoordinate(new Coordinate(5, 3)));
    }

    /**
     * Test Case: Verify coordinate membership check.
     * * Type: Black Box Testing
     * * Technique: Equivalence Partitioning - Non-Member.
     * * Description: Verifies that occupiesCoordinate returns false for coordinates
     * that are not part of the ship, protecting against false positives in collision detection.
     */
    @Test
    public void testOccupiesCoordinateNotFound() {
        Ship ship = new Ship(ShipType.SUBMARINE);
        ship.setPosition(new Coordinate(2, 2), Orientation.VERTICAL);

        assertFalse(ship.occupiesCoordinate(new Coordinate(1, 2)));
        assertFalse(ship.occupiesCoordinate(new Coordinate(2, 5)));
        assertFalse(ship.occupiesCoordinate(new Coordinate(5, 5)));
    }

    /**
     * Test Case: Verify ship type retrieval.
     * * Type: Unit Testing
     * * Technique: Statement Coverage.
     */
    @Test
    public void testGetType() {
        Ship ship = new Ship(ShipType.CARRIER);
        assertEquals(ShipType.CARRIER, ship.getType());
    }
}
