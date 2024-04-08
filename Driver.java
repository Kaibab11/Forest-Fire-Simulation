import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import World.Wind;
import World.World;

public class Driver {
    public static void main(String[] args) {
        // Default values
        int defaultGridSize = 150;
        int defaultCellSize = 5;

        // Prompt the user for gridSize and validate the input
        int gridSize = validateInput("Enter the grid size (default is " + defaultGridSize + "):", defaultGridSize);
        if (gridSize == -1) {
            System.exit(0); // Exit the program if user cancels
        }

        // Prompt the user for cellSize and validate the input
        int cellSize = validateInput("Enter the cell size (default is " + defaultCellSize + "):", defaultCellSize);
        if (cellSize == -1) {
            System.exit(0); // Exit the program if user cancels
        }

        Wind wind = new Wind(null, null);
        World world = new World(gridSize, wind); // Create a World instance

        // Create and run the GUI
        SwingUtilities.invokeLater(() -> {
            @SuppressWarnings("unused")
            Simple_Graphics graphicsPanel = new Simple_Graphics(world, cellSize);
        });
    }

    private static int validateInput(String message, int defaultValue) {
        while (true) {
            String input = (String) JOptionPane.showInputDialog(
                null, message, "Input", JOptionPane.PLAIN_MESSAGE, null, null, defaultValue + ""
            );
            if (input == null) { // User pressed cancel or closed the dialog
                return -1; // Value to indicate cancellation
            }
            try {
                int value = Integer.parseInt(input);
                if (value <= 0) {
                    JOptionPane.showMessageDialog(null, "Please enter a positive integer.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid integer.");
            }
        }
    }
}
