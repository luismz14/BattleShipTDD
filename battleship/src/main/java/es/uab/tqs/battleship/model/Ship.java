package es.uab.tqs.battleship.model;
import java.util.ArrayList;
import java.util.List;

public class Ship {
    private final ShipType type;
    private final List<Coordinate> coordinates;
    private int hitCount;
    private Orientation orientation;


    public Ship(ShipType type) {
        this.type = type;
        this.coordinates = new ArrayList<>();
        this.hitCount = 0;
        this.orientation = null;
    }


    public ShipType getType() {
        return type;
    }


    public int getLength() {
        return type.getLength();
    }

    public List<Coordinate> getCoordinates() {
        return new ArrayList<>(coordinates);
    }


    public void setPosition(Coordinate startCoordinate, Orientation orientation) {
        this.orientation = orientation;
        this.coordinates.clear();

        for (int i = 0; i < getLength(); i++) {
            int x = startCoordinate.getX();
            int y = startCoordinate.getY();

            if (orientation == Orientation.HORIZONTAL) {
                x += i;
            } else {
                y += i;
            }

            coordinates.add(new Coordinate(x, y));
        }
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void registerHit() {
        hitCount++;
    }


    public int getHitCount() {
        return hitCount;
    }


    public boolean isSunk() {
        return hitCount >= getLength();
    }


    public boolean occupiesCoordinate(Coordinate coordinate) {
        for (Coordinate coord : coordinates) {
            if (coord.equals(coordinate)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return type.getDisplayName() + " (" + getLength() + " cells)";
    }
}
