package es.uab.tqs.battleship.model;

/**
 * Enumeration representing the different types of ships available in the game.
 * Each ship type has a predefined length and a display name.
 */
public enum ShipType {
    CARRIER(5, "Carrier"),
    BATTLESHIP(4, "Battleship"),
    CRUISER(3, "Cruiser"),
    SUBMARINE(3, "Submarine"),
    DESTROYER(2, "Destroyer");

    private final int length;
    private final String displayName;

    /**
     * Constructor for the ShipType enum.
     *
     * @param length      The length of the ship.
     * @param displayName The display name of the ship.
     */
    ShipType(int length, String displayName) {
        this.length = length;
        this.displayName = displayName;
    }

    /**
     * Gets the length of the ship.
     *
     * @return The length in cells.
     */
    public int getLength() {
        return length;
    }

    /**
     * Gets the display name of the ship.
     *
     * @return The name of the ship.
     */
    public String getDisplayName() {
        return displayName;
    }
}