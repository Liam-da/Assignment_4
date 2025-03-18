package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConveyorBeltTest {

    private Board board;
    private GameController gameController;
    private ConveyorBelt conveyorBelt;
    private Player player;

    @BeforeEach
    void setUp() {
        board = new Board(8, 8);
        gameController = new GameController(board);
        conveyorBelt = new ConveyorBelt();
        conveyorBelt.setHeading(Heading.EAST);

        // Create a player at (3,3) stepping onto conveyor belt
        player = new Player(board, "red", "TestPlayer");
        board.addPlayer(player);
        Space startSpace = board.getSpace(3, 3);
        startSpace.addAction(conveyorBelt);
        player.setSpace(startSpace);
    }

    /**
     * Test that the conveyor belt is initialized correctly.
     */
    @Test
    void testConveyorBeltInitialization() {
        assertNotNull(conveyorBelt, "ConveyorBelt should be initialized.");
        assertEquals(Heading.EAST, conveyorBelt.getHeading(), "ConveyorBelt should default to EAST.");
    }

    /**
     * Test that the conveyor belt can be set to a different heading.
     */
    @Test
    void testSetAndGetHeading() {
        conveyorBelt.setHeading(Heading.NORTH);
        assertEquals(Heading.NORTH, conveyorBelt.getHeading(), "Heading should be NORTH after setting.");
    }

    /**
     * Test that the conveyor belt moves the player correctly.
     */
    @Test
    void testConveyorBeltMovesPlayer() {
        Space expectedSpace = board.getSpace(4, 3); // Expected destination
        conveyorBelt.doAction(gameController, player.getSpace());
        assertEquals(expectedSpace, player.getSpace(), "Player should be pushed EAST.");
    }

    /**
     * Test that the conveyor belt does not move the player if the next space is occupied.
     */
    @Test
    void testConveyorBeltDoesNotMovePlayerWhenBlocked() {
        Player blockingPlayer = new Player(board, "blue", "Blocker");
        board.addPlayer(blockingPlayer);
        Space blockedSpace = board.getSpace(4, 3);
        blockingPlayer.setSpace(blockedSpace);

        conveyorBelt.doAction(gameController, player.getSpace());
        assertEquals(board.getSpace(3, 3), player.getSpace(), "Player should not move because the destination is occupied.");
    }
}
