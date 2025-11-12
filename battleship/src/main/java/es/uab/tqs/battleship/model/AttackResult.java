package es.uab.tqs.battleship.model;

public enum AttackResult {
    HIT("Hit!"),
    MISS("Water!"),
    SUNK("You sunk a ship!"),
    ALREADY_ATTACKED("Already attacked this cell.");

    private final String message;

    AttackResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
