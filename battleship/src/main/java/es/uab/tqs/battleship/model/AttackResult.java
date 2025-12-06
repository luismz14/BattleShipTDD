package es.uab.tqs.battleship.model;

/**
 * Represents the possible outcomes of an attack on a coordinate.
 * This enum encapsulates the result status along with a user-friendly message.
 */
public enum AttackResult {

    /**
     * The attack hit a ship.
     */
    HIT("Hit!"),

    /**
     * The attack missed and landed in water.
     */
    MISS("Water!"),

    /**
     * The attack hit a ship and caused it to sink (all its coordinates are hit).
     */
    SUNK("You sunk a ship!"),

    /**
     * The target coordinate had already been attacked previously.
     */
    ALREADY_ATTACKED("Already attacked this cell.");

    /**
     * A descriptive message associated with the attack result.
     */
    private final String message;

    /**
     * Constructor for the enum constant.
     *
     * @param message The descriptive message for the result.
     */
    AttackResult(String message) {
        this.message = message;
    }

    /**
     * Retrieves the descriptive message of the attack result.
     *
     * @return The message string.
     */
    public String getMessage() {
        return message;
    }
}