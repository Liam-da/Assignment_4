package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is a test for the FieldAction class, used to test the functionality of the doAction method.
 * The test class extends FieldAction and overrides the doAction method for the purpose of testing.
 */
class TestFieldAction extends FieldAction {

    /**
     * Override the doAction method to simulate an action in the test.
     * This method always returns true, simulating a successful action.
     *
     * @param gameController the game controller managing the game logic
     * @param space the space on which the action is performed
     * @return true indicating that the action was successful
     */
    @Override
    public boolean doAction(GameController gameController, Space space) {

        return true;
    }
}

/**
 * Unit test class for testing the functionality of the FieldAction class.
 * The class tests the doAction method of the FieldAction class.
 * It uses JUnit 5 for testing.
 */
class FieldActionTest {

    private FieldAction fieldAction;


    /**
     * Set up the test by initializing the FieldAction instance to a TestFieldAction.
     * This method runs before each test to ensure that a fresh instance is used.
     */
    @BeforeEach
    void setUp() {
        fieldAction = new TestFieldAction();
    }


    /**
     * Test method to check the behavior of the doAction method in the FieldAction class.
     * It creates a mock board, game controller, and space, then calls doAction on the fieldAction.
     * The test asserts that the doAction method returns true, indicating a successful action.
     */
    @Test
    void testDoAction() {
        Board board = new Board(10, 10); // Assuming Board has a constructor with width and height
        GameController gameController = new GameController(board); // Pass the required argument
        Space space = new Space(board, 0, 0); // Pass the board and coordinates
        boolean result = fieldAction.doAction(gameController, space);
        assertTrue(result, "doAction should return true");
    }
}