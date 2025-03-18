package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;
    private GameController gameController;
    private Board board;
    private Player player;

    @BeforeEach
    void setUp() {
        board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        player = new Player(board, "red", "Player 1");
        board.addPlayer(player);
        player.setSpace(board.getSpace(0, 0));
        player.setHeading(Heading.NORTH);
        board.setCurrentPlayer(player);
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }

    @Test
    void testSetup() {
        assertNotNull(board, "Board is not initialized correctly!");
        assertNotNull(gameController, "GameController is not initialized correctly!");
    }

    @Test
    void testMoveCurrentPlayerToSpace() {
        Space targetSpace = board.getSpace(0, 4);
        assertNotNull(targetSpace, "Target space should not be null!");
        targetSpace.setPlayer(null);

        gameController.moveCurrentPlayerToSpace(targetSpace);
        assertEquals(player, targetSpace.getPlayer(), "Player should be on space (0,4)!");
    }

    @Test
    void testMoveForward() {
        Space startSpace = player.getSpace();
        Space expectedNextSpace = board.getNeighbour(startSpace, player.getHeading());

        if (expectedNextSpace != null) {
            boolean moved = gameController.moveForward(player);
            assertTrue(moved, "Player should have moved forward!");
            assertEquals(player, expectedNextSpace.getPlayer(), "Player should now be on the new space!");
        } else {
            System.out.println("No space in front of the player - skipping move test.");
        }
    }

    @Test
    void testMoveForwardIntoWall() {
        Space startSpace = board.getSpace(2, 2);
        assertNotNull(startSpace, "Start space should not be null!");
        player.setSpace(startSpace);
        player.setHeading(Heading.NORTH);

        startSpace.addWall(Heading.NORTH);
        boolean moved = gameController.moveForward(player);
        assertFalse(moved, "Player should not be able to move into a wall!");
        assertEquals(player, startSpace.getPlayer(), "Player should remain in the same space!");
    }

    @Test
    void testTurnLeft() {
        player.setHeading(Heading.EAST);
        gameController.turnLeft(player);
        assertEquals(Heading.NORTH, player.getHeading(), "Player should face NORTH after turning left!");
    }

    @Test
    void testTurnRight() {
        player.setHeading(Heading.EAST);
        gameController.turnRight(player);
        assertEquals(Heading.SOUTH, player.getHeading(), "Player should face SOUTH after turning right!");
    }

    @Test
    void testMoveBackward() {
        Space startSpace = board.getSpace(3, 3);
        assertNotNull(startSpace, "Start space should not be null!");
        player.setSpace(startSpace);
        player.setHeading(Heading.NORTH);

        Space expectedSpace = board.getNeighbour(startSpace, Heading.SOUTH);
        if (expectedSpace == null) {
            System.out.println("No space behind player - skipping backward test.");
            return;
        }

        boolean moved = gameController.backward(player);
        assertTrue(moved, "Player should have moved backward!");
        assertEquals(player, expectedSpace.getPlayer(), "Player should now be in the space behind!");
    }

    @Test
    void testUTurn() {
        player.setSpace(board.getSpace(4, 4));
        player.setHeading(Heading.NORTH);
        gameController.uTurn(player);
        assertEquals(Heading.SOUTH, player.getHeading(), "Player should be facing SOUTH after U-Turn!");
        gameController.uTurn(player);
        assertEquals(Heading.NORTH, player.getHeading(), "Player should be facing NORTH after second U-Turn!");
    }
}
