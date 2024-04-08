package World;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import Cell.Cell;

public class WorldTest {
    private World world;

    @Before
    public void setUp() {
        world = new World(10, new Wind(Wind.Direction.NORTH, Wind.Strength.WEAK));
    }

    @Test
    public void testInitialization() {
        assertNotNull(world.getPresentGrid());
        assertNotNull(world.getFutureGrid());
        assertEquals(10, world.getSize());
        assertNotNull(world.getWind());
    }

    @Test
    public void testCopyPresentToFuture() {
        Cell[][] initialPresentGrid = world.getPresentGrid();
        Cell[][] initialFutureGrid = world.getFutureGrid();
        world.copyPresentToFuture();
        assertArrayEquals(initialPresentGrid, initialFutureGrid);
    }

    @Test
    public void testCopyFutureToPresent() {
        Cell[][] initialPresentGrid = world.getPresentGrid();
        Cell[][] initialFutureGrid = world.getFutureGrid();
        world.copyFutureToPresent();
        assertArrayEquals(initialFutureGrid, initialPresentGrid);
    }

    @Test
    public void testUpdate() {

    }
}
