package Cell;
import java.awt.Color;

public abstract class Tree extends Cell {
    private int age;

    public Tree() {
        super(Cell.state.TREE);
        this.age = 0; // Initial age is set to 0
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void grow() {
        this.age++;
    }

    public abstract Color getColor(); // Abstract method for getting color

    public abstract double getBurnProbability(); // Abstract method for getting burn probability

    public static class Pine extends Tree {
        private static final Color YOUNG_COLOR = new Color(34, 139, 34); // Young Pine trees are medium green
        private static final Color MATURE_COLOR = new Color(0, 100, 0); // Mature Pine trees are dark green
        private static final Color OLD_COLOR = new Color(138, 128, 0); // Old Pine trees are olive green
        private static final double YOUNG_BURN_PROBABILITY = 0.2; // Higher burning chance for young Pine trees
        private static final double MATURE_BURN_PROBABILITY = 0.1; // Medium burning chance for mature Pine trees
        private static final double OLD_BURN_PROBABILITY = 0.05; // Lower burning chance for old Pine trees

        public Pine() {
            super();
        }

        @Override
        public Color getColor() {
            if (getAge() < 30) {
                return YOUNG_COLOR; // Young trees are medium green
            } else if (getAge() < 500) {
                return MATURE_COLOR; // Mature trees are dark green
            } else {
                return OLD_COLOR; // Old trees are brown
            }
        }

        @Override
        public double getBurnProbability() {
            if (getAge() < 30) {
                return YOUNG_BURN_PROBABILITY; // Higher burning chance for young trees
            } else if (getAge() < 500) {
                return MATURE_BURN_PROBABILITY; // Medium burning chance for mature trees
            } else {
                return OLD_BURN_PROBABILITY; // Lower burning chance for old trees
            }
        }
    }

    public static class Maple extends Tree {
        private static final Color YOUNG_COLOR = new Color(178, 255, 102); // Young Maple trees are lighterr green
        private static final Color MATURE_COLOR = new Color(0, 255, 0); // Mature Maple trees are light green
        private static final Color OLD_COLOR = new Color(139, 69, 19); // Old Maple trees are brown
        private static final double YOUNG_BURN_PROBABILITY = 0.2; // Higher burning chance for young Maple trees
        private static final double MATURE_BURN_PROBABILITY = 0.1; // Medium burning chance for mature Maple trees
        private static final double OLD_BURN_PROBABILITY = 0.05; // Lower burning chance for old Maple trees

        public Maple() {
            super();
        }

        @Override
        public Color getColor() {
            if (getAge() < 30) {
                return YOUNG_COLOR; // Young trees are light green
            } else if (getAge() < 500) {
                return MATURE_COLOR; // Mature trees are dark green
            } else {
                return OLD_COLOR; // Old trees are brown
            }
        }

        @Override
        public double getBurnProbability() {
            if (getAge() < 30) {
                return YOUNG_BURN_PROBABILITY; // Higher burning chance for young trees
            } else if (getAge() < 500) {
                return MATURE_BURN_PROBABILITY; // Medium burning chance for mature trees
            } else {
                return OLD_BURN_PROBABILITY; // Lower burning chance for old trees
            }
        }
    }
}
