package World;
import java.util.Random;

import Cell.Ash;
import Cell.Cell;
import Cell.Fire;
import Cell.Tree;

public class World {
    private Cell[][] presentGrid;
    private Cell[][] futureGrid;
    private int size;
    private Wind wind;
    private boolean spawnFire = false;

    /**
     * Constructs a World object with the specified size and probability of catching fire.
     * 
     * @param size The size of the grid.
     */    
    public World(int size, Wind wind) {
        this.size = size;
        this.wind = wind;
        presentGrid = new Cell[this.size][this.size];
        futureGrid = new Cell[this.size][this.size];
        initializeEmpty();
    }
    
    /**
     * Gets the present grid.
     * 
     * @return The present grid.
     */    
    public Cell[][] getPresentGrid() {
        return presentGrid;
    }

    /**
     * Gets the future grid.
     * 
     * @return The future grid.
     */    
    public Cell[][] getFutureGrid() {
        return futureGrid;
    }

    /**
     * Gets the size of the grid.
     * 
     * @return The size of the grid.
     */    
    public int getSize() {
        return size;
    }

    /**
     * Gets the current wind object for the world
     * 
     * @return The wind of the world
     */
    public Wind getWind() {
        return this.wind;
    }

    /**
     * Sets the spawn fire boolean value.
     * 
     * @param bol The new true/false value
     */
    public void setSpawnFire(boolean bol) {
        this.spawnFire = bol;
    }

    /**
     * Copies the present grid to the future grid.
     */
    public void copyPresentToFuture() {
        copyGrid(presentGrid, futureGrid);
    }

    /**
     * Copies the future grid to the present grid.
     */
    public void copyFutureToPresent() {
        copyGrid(futureGrid, presentGrid);
    }

    /**
     * Updates the grid. 
     * First checks the current state of the cell (EMPTY, TREE, BURNING, ASH, WATER).
     * Then if the state is either ASH or Burning, will age down at random and if the age is 0 will be turned into an empty cell.
     * If the cell is EMPTY then there is a chance for a new TREE to grow in that cell
     * If the cell is a TREE,  
     * 
     */    
    public void update() {
        Random randomNumber = new Random();
        int randomRow = spawnFire ? randomNumber.nextInt(this.size) : 0;
        int randomColumn = spawnFire ? randomNumber.nextInt(this.size) : 0;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                
                Cell currentCell = presentGrid[i][j];
                Cell.state currentState = currentCell.getState();

                if (currentState == Cell.state.TREE) {
                    Tree treeCell = (Tree) currentCell;
                    treeCell.grow(); // Increment tree age

                    if (spawnFire && i == randomRow && j == randomColumn) {
                        futureGrid[i][j] = new Fire();
                        this.spawnFire = false;
                        continue;
                    }

                    int[] burning = wind.getStrength() == null ? neighborIsBurning(i,j) : neighborIsBurningWithWind(i,j);
                    int burningCheck = burning[0];
                    int burningAmount = burning[1];
                    int windCheck = burning[2];

                    if (burningCheck == 1) {

                        double random = Math.random();
                        double adjustProbability = adjustProbability(treeCell.getBurnProbability(), burningAmount);
                        if (this.wind.getStrength() != null) {
                            if (windCheck == 1 && random < wind.windBurnChanceIncrease() * adjustProbability) {
                                futureGrid[i][j] = new Fire(); 
                            }else if (random < wind.windBurnChanceDecrease() * adjustProbability) {
                                futureGrid[i][j] = new Fire(); 
                            }
                        }else if (random < adjustProbability) {
                            futureGrid[i][j] = new Fire(); 
                        }
                    } else {
                        futureGrid[i][j] = treeCell;  
                    }
                } else if (currentState == Cell.state.EMPTY) {

                    double newTreeChance = 0.001;
                    double random  = Math.random();

                    if (neighborIsWater(i, j))
                        newTreeChance += 0.01;
                    if (random < newTreeChance) {
                        futureGrid[i][j] = new Tree.Maple();
                    } else if (random < newTreeChance * 2) {
                        futureGrid[i][j] = new Tree.Pine();
                    } else {
                        futureGrid[i][j] = currentCell;
                    }
                } else if (currentState == Cell.state.BURNING) {
                    Fire fireCell = (Fire) currentCell;
                    fireCell.progressFire();
                    if (fireCell.getFireAge() == 0) {
                        futureGrid[i][j] = new Ash();
                    }else {
                        futureGrid[i][j] = fireCell;
                    }
                } else if (currentState == Cell.state.ASH) {
                    Ash ashCell = (Ash) currentCell;
                    ashCell.disperseAsh();
                    if (ashCell.getAshAge() == 0)
                        futureGrid[i][j] = new Cell(Cell.state.EMPTY);
                    else
                        futureGrid[i][j] = ashCell;

                }
            }
        }
        copyFutureToPresent();
    }

    /**
     * Initializes the grid 
     */
    private void initializeEmpty() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                    presentGrid[i][j] = new Cell(Cell.state.EMPTY);
                    futureGrid[i][j] = new Cell(Cell.state.EMPTY);
            }
        }
    }

    /**
     * Checks if neighboring cells are burning and returns the result.
     *
     * @param row    The row index of the cell.
     * @param column The column index of the cell.
     * @return An array containing the burning check result and the number of adjacent burning cells. 
     * index 0 = 0/1 False/True is a neighboor burning, index 1 = the number of adjacent burning cells, index 2 = 0/1 False/True should wind effect the burning of this cell
     */
    private int[] neighborIsBurning(int row, int column) {
        boolean[] directions = new boolean[8]; // Initialize all directions to false
        int trueFalse = 0; // 0 = False, 1 = True
        int count = 0;

        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (i == 0 && j == 0) continue; // Skip the current cell
                int newRow = row + i;
                int newColumn = column + j;
                // Check if the new row and column are within bounds
                if (isValidCell(newRow, newColumn) && presentGrid[newRow][newColumn].getState() == Cell.state.BURNING) {
                    trueFalse = 1;
                    count++;
                    directions[calculateDirection(i, j)] = true;
                }
            }
        }
        int[] tempArray = {trueFalse, count, 0};
        return tempArray;
    }

    /**
     * Checks if neighboring cells are burning and returns the result. 
     * Should only be called if wind is not null.
     * Depending on the wind strength will check farther for burning cells if neigboor cell in the 
     * direction of the wind is empty.
     *
     * @param row    The row index of the cell.
     * @param column The column index of the cell.
     * @return An array containing the burning check result and the number of adjacent burning cells. 
     * index 0 = 0/1 False/True is a neighboor burning, index 1 = the number of adjacent burning cells, index 2 = 0/1 False/True should wind effect the burning of this cell
     */
    private int[] neighborIsBurningWithWind(int row, int column) {
        boolean[] directions = new boolean[8]; // Initialize all directions to false
        int trueFalse = 0; // 0 = False, 1 = True
        int count = 0;

        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (i == 0 && j == 0) continue; // Skip the current cell
                int newRow = row + i;
                int newColumn = column + j;
                // Check if the new row and column are within bounds
                if (isValidCell(newRow, newColumn)) {
                    if (presentGrid[newRow][newColumn].getState() == Cell.state.BURNING) {
                        trueFalse = 1;
                        count++;
                        directions[calculateDirection(i, j)] = true;
                    } else if (presentGrid[newRow][newColumn].getState() == Cell.state.EMPTY && calculateDirection(i, j) == wind.getDirection().ordinal()) {
                        int temp = 1;
                        if (burningRecursive(newRow, newColumn, i, j, temp)) {
                            trueFalse = 1;
                            directions[calculateDirection(i, j)] = true;
                        }
                    }
                }
            }
        }

        if (trueFalse == 1) {
            int[] tempArray = {trueFalse, count, isWindDirection(directions)};
            return tempArray;
        } else {
            int[] tempArray = {trueFalse, count, 0};
            return tempArray;
        }

    }


    /**
     * Checks if the cell in the direction that the wind is blowing is burning, or will check if it is empty and will then call itself.
     * 
     * @param row the newRow value from the neigborhood is burning method.
     * @param column the new Column from the neigborhood is burning method.
     * @param i the row in relation to the current cell
     * @param j the column in relation to the current cell
     * @param temp a temp value to check the amount of calls the recursive function makes depending on the wind strength
     * @return true if the cell is burning else either calls the function again or returns false if the temp value is greater than either 1 or 6 depending on the current wind strength. 
     */
    private boolean burningRecursive (int row, int column, int i, int j, int temp) {

        int newRow = row + (i * temp);
        int newColumn = column + (j * temp);
        if (isValidCell(newRow, newColumn)) {
            if (presentGrid[newRow][newColumn].getState() == Cell.state.BURNING) {
                return true;
            } else if (presentGrid[newRow][newColumn].getState() == Cell.state.EMPTY) {
                temp++;
                if (wind.getStrength() == Wind.Strength.STRONG ? temp > 6 : temp > 1) {
                    return false;
                } else 
                    return burningRecursive(row, column, i, j, temp);
            }
        }
        return false;     
    }

    /**
     * Checking if the burnning cell is in the direction of the wind based of the current cell.
     * 
     * @param directions the boolean array of whether the wind is in that direction based of the index of the array and the ordinal of the wind strength.
     * @return 0 if not in the direction and 1 if the cell is in the dirrection of the wind
     */
    private int isWindDirection(boolean[] directions) {
        Wind.Direction currentDirection = wind.getDirection();
        for (int i = 0; i < 8 ;++i) {
            if (currentDirection.ordinal() == i && directions[i]) {
                return 1;
            }
        }
        return 0;
    }    

    /**
     * Calculates the direction the burning cell is in relation to the current cell.
     * 
     * @param i the row in relation to the current cell
     * @param j the column in relation to the current cell
     * @return the ordnal of the direction of the wind
     */
    private int calculateDirection(int i, int j) {
        if (i == -1) {
            if (j == -1) return 3;  //NW
            if (j == 0) return 4;  //N
            if (j == 1) return 5;  //NE
        } else if (i == 0) {
            if (j == -1) return 2; //W
            if (j == 1) return 6;  //E
        } else if (i == 1) {
            if (j == -1) return 1; //SW
            if (j == 0) return 0;  //S
            if (j == 1) return 7;  //SE
        }
        return -1; // Invalid direction or no burning cell in that direction
    }

    /**
     * Checks if the neigbor of the current cell is water in a 7x7 grid around it.
     * 
     * @param row current row
     * @param column current column
     * @return  boolean value true if there is water, false if there is no water.
     */
    private boolean neighborIsWater(int row, int column) {
        for (int i = -3; i <= 3; ++i) {
            for (int j = -3; j <= 3; ++j) {
                if (Math.abs(i) + Math.abs(j) > 3) continue; //  the corners
                if (i == 0 && j == 0) continue; // Skip the current cell
                int newRow = row + i;
                int newColumn = column + j;

                if (isValidCell(newRow, newColumn) && presentGrid[newRow][newColumn].getState() == Cell.state.WATER) {
                    return true;
                }
            }
        }
        return false;         
    }

    /**
     * Copies the contents of one grid to another.
     * 
     * @param sourceGrid The grid to copy from.
     * @param destinationGrid The grid to copy to.
     */
    private void copyGrid(Cell[][] sourceGrid, Cell[][] destinationGrid) {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                Cell cellToCopy = sourceGrid[i][j];
                destinationGrid[i][j] = cellToCopy;
            }
        }
    }
    
    /**
     * Helper method to check if the cell coordinates are within the grid bounds
     * 
     * @param row current row
     * @param column current column
     * @return  boolean if the cell is a cell that exists in the grid.
     */
    private boolean isValidCell(int row, int column) {
        return row >= 0 && row < presentGrid.length && column >= 0 && column < presentGrid[0].length;
    }

    /**
     * Adjusts the probability of a tree catching fire based on the number of adjacent burning trees.
     * 
     * @param prob The initial probability of catching fire.
     * @param factor The factor by which the probability is adjusted.
     * @return The adjusted probability.
     */
    private static double adjustProbability(double prob, double factor) {
        double adjustedProb = prob * Math.log((3 * factor) + 1);
        return adjustedProb;
    }
    
}
