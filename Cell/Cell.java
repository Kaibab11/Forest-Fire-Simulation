package Cell;
import java.awt.Color;

/**
 * Represents a cell in the forest fire simulation grid.
 */
public class Cell {
    // Static constants for cell states

    public enum state {
        EMPTY, TREE, BURNING, ASH, WATER
    }

    private state state; // Represents the state of the cell
    private Color color = Color.WHITE; // The color of the cell

    /**
     * Constructs a cell with the given state.
     * 
     * @param state The initial state of the cell (EMPTY, TREE, BURNING, ASH, WATER).
     * @param color The initial color of the cell.
     */
    public Cell(state state) {
        this.state = state;
    }

    /**
     * Gets the state of the cell.
     * 
     * @return The state of the cell (EMPTY, TREE, BURNING, ASH, WATER).
     */
    public state getState() {
        return state;
    }

    /**
     * Sets the state of the cell.
     * 
     * @param state The new state of the cell (EMPTY, TREE, BURNING, ASH, WATER).
     */
    public void setState(state state) {
        this.state = state;
    }

    /**
     * Gets the color associated with the current state of the cell.
     * 
     * @return The color corresponding to the cell's state.
     */
    public Color getColor() {
        return this.color;
    }
}


