package dk.dtu.compute.se.pisd.roborally.controller;
import dk.dtu.compute.se.pisd.roborally.model.Space;


import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;
    private GameController gameController;
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);

        //  Add at least one player to avoid NullPointerException
        Player player = new Player(board, "red", "Player 1");
        board.addPlayer(player);

        // Ensure player has a valid starting space
        Space startSpace = board.getSpace(0, 0);
        assertNotNull(startSpace, "Start space should not be null!");
        player.setSpace(startSpace);

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
    void testV1() {
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();

        //  Ensure the current player exists before using it
        assertNotNull(player, "Current player should not be null!");

        Space targetSpace = board.getSpace(0, 4);
        assertNotNull(targetSpace, "Target space (0,4) should not be null!");

        gameController.moveCurrentPlayerToSpace(targetSpace);

        Assertions.assertEquals(player, targetSpace.getPlayer(),
                "Player " + player.getName() + " should be on Space (0,4)!");
    }
    @Test
    void testMoveForward() {
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();

        // Ensure the player starts on a valid space
        assertNotNull(player.getSpace(), "Player should start on a valid space!");

        //  Get the player's current position
        Space startSpace = player.getSpace();
        Space expectedNextSpace = board.getNeighbour(startSpace, player.getHeading());

        //  Check if there is a valid space in front of the player
        if (expectedNextSpace != null) {
            boolean moved = gameController.moveForward(player);

            //  The player should have moved
            assertTrue(moved, "Player should have moved forward!");

            //  The player should now be on the new space
            assertEquals(player, expectedNextSpace.getPlayer(), "Player should now be on the new space!");
        } else {
            System.out.println("No space in front of the player - skipping move test.");
        }
    }
    @Test
    void testMoveForwardIntoWall() {
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();

        // Place player on a valid space
        Space startSpace = board.getSpace(2, 2);
        assertNotNull(startSpace, "Start space should not be null!");
        player.setSpace(startSpace);

        //  Make sure the player faces a wall
        player.setHeading(Heading.NORTH);
        startSpace.getWalls().add(Heading.NORTH); // Add a wall in front

        //  Try to move forward (should fail)
        boolean moved = gameController.moveForward(player);

        //  Player should not move
        assertFalse(moved, "Player should not be able to move into a wall!");

        //  Player should still be in the same space
        assertEquals(player, startSpace.getPlayer(), "Player should remain in the same space!");
    }
    @Test
    void testTurnLeft() {
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();

        // Set player to face EAST
        player.setHeading(Heading.EAST);

        //  Rotate left (should now face NORTH)
        gameController.turnLeft(player);

        //  Player should now face NORTH
        assertEquals(Heading.NORTH, player.getHeading(), "Player should face NORTH after turning left!");

        //  Player should remain in the same space
        assertEquals(player, player.getSpace().getPlayer(), "Player should remain in the same space!");
    }
    @Test
    void testTurnRight() {
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();

        //  Set player to face EAST
        player.setHeading(Heading.EAST);

        //  Rotate right (should now face SOUTH)
        gameController.turnRight(player);

        //  Player should now face SOUTH
        assertEquals(Heading.SOUTH, player.getHeading(), "Player should face SOUTH after turning right!");

        //  Player should remain in the same space
        assertEquals(player, player.getSpace().getPlayer(), "Player should remain in the same space!");
    }
    @Test
    void testMoveBackward() {
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();

        //  Place player on a valid space
        Space startSpace = board.getSpace(3, 3);
        assertNotNull(startSpace, "Start space should not be null!");
        player.setSpace(startSpace);
        player.setHeading(Heading.NORTH); // ✅ Facing NORTH

        //  Get the space behind the player (should be SOUTH)
        Space expectedSpace = board.getNeighbour(startSpace, Heading.SOUTH);
        assertNotNull(expectedSpace, "There should be a space behind the player!");

        //  Move backward (should succeed)
        boolean moved = gameController.backward(player);

        //  The player should have moved
        assertTrue(moved, "Player should have moved backward!");

        //  The player should now be on the space behind
        assertEquals(player, expectedSpace.getPlayer(), "Player should now be in the space behind!");
    }
    @Test
    void testUTurn() {
        // Arrange: Create a board and a player
        Board board = new Board(8, 8);
        GameController gameController = new GameController(board);
        Player player = new Player(board, "red", "Player 1");
        board.addPlayer(player);

        // Place the player on a space and set an initial heading
        Space startSpace = board.getSpace(4, 4);
        assertNotNull(startSpace, "Start space should not be null!");
        player.setSpace(startSpace);
        player.setHeading(Heading.NORTH); // The player starts facing NORTH

        // Act: Perform a U-Turn
        gameController.uTurn(player);

        // Assert: The player should now be facing SOUTH
        assertEquals(Heading.SOUTH, player.getHeading(), "Player should be facing SOUTH after U-Turn!");

        // Act: Perform another U-Turn
        gameController.uTurn(player);

        // Assert: The player should now be back to facing NORTH
        assertEquals(Heading.NORTH, player.getHeading(), "Player should be facing NORTH after second U-Turn!");
    }


















    // TODO: Add more tests for assignment V2
}
