package es.uab.tqs.battleship.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

public class ShipTest {

    @ParameterizedTest
    @EnumSource(ShipType.class)
    public void testShipTypeLength(ShipType type) {
        Ship ship = new Ship(type);
        assertEquals(type.getLength(), ship.getLength());
    }

    @Test
    public void testMinShipLength() {
        Ship ship = new Ship(ShipType.DESTROYER);
        assertEquals(2, ship.getLength());
    }

    @Test
    public void testMaxShipLength() {
        Ship ship = new Ship(ShipType.CARRIER);
        assertEquals(5, ship.getLength());
    }

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

    @Test
    public void testInitialHitCountIsZero() {
        Ship ship = new Ship(ShipType.CARRIER);
        assertEquals(0, ship.getHitCount());
        assertFalse(ship.isSunk());
    }

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

    @Test
    public void testShipSunkAtExactLimit() {
        Ship ship = new Ship(ShipType.DESTROYER);

        ship.registerHit();
        assertFalse(ship.isSunk());

        ship.registerHit();
        assertTrue(ship.isSunk());
    }

    @Test
    public void testOccupiesCoordinateFound() {
        Ship ship = new Ship(ShipType.CRUISER);
        ship.setPosition(new Coordinate(3, 3), Orientation.HORIZONTAL);

        assertTrue(ship.occupiesCoordinate(new Coordinate(3, 3)));
        assertTrue(ship.occupiesCoordinate(new Coordinate(4, 3)));
        assertTrue(ship.occupiesCoordinate(new Coordinate(5, 3)));
    }

    @Test
    public void testOccupiesCoordinateNotFound() {
        Ship ship = new Ship(ShipType.SUBMARINE);
        ship.setPosition(new Coordinate(2, 2), Orientation.VERTICAL);

        assertFalse(ship.occupiesCoordinate(new Coordinate(1, 2)));
        assertFalse(ship.occupiesCoordinate(new Coordinate(2, 5)));
        assertFalse(ship.occupiesCoordinate(new Coordinate(5, 5)));
    }

    @Test
    public void testGetType() {
        Ship ship = new Ship(ShipType.CARRIER);
        assertEquals(ShipType.CARRIER, ship.getType());
    }
}
