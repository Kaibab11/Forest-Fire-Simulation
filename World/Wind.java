package World;
public class Wind {
    public enum Direction {
        NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST
    }

    public enum Strength {
        WEAK, MODERATE, STRONG
    }

    private Direction direction;
    private Strength strength;

    public Wind(Direction direction, Strength strength) {
        this.direction = direction;
        this.strength = strength;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Strength getStrength() {
        return strength;
    }

    public void setStrength(Strength strength) {
        this.strength = strength;
    }

    public double windBurnChanceIncrease() {
        double increaseFactor = 0.0; // Increase factor based on wind strength

        switch (strength) {
            case WEAK:
                increaseFactor = 2.5;
                break;
            case MODERATE:
                increaseFactor = 4;
                break;
            case STRONG:
                increaseFactor = 10;
                break;
        }

        return increaseFactor;
    }

    public double windBurnChanceDecrease() {
        double decreaseFactor = 0.0; // Decrease factor based on wind strength

        switch (strength) {
            case WEAK:
                decreaseFactor = 0.7;
                break;
            case MODERATE:
                decreaseFactor = 0.4;
                break;
            case STRONG:
                decreaseFactor = 0.2;
                break;
        }

        return decreaseFactor;
    }
}