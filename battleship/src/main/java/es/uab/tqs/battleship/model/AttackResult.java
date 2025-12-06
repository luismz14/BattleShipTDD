package es.uab.tqs.battleship.model;

/**
 * Represents the possible outcomes of an attack on a coordinate.
 * This enum encapsulates the result status along with a user-friendly message.
 */
public enum AttackResult {
    HIT("Hit!"),
    MISS("Water!"),
    SUNK("You sunk a ship!"),
    ALREADY_ATTACKED("Already attacked this cell.");

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