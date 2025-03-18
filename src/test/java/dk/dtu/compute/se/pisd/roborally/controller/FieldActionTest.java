package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestFieldAction extends FieldAction {
    @Override
    public boolean doAction(GameController gameController, Space space) {
        // Implement a simple test action
        return true;
    }
}

class FieldActionTest {

    private FieldAction fieldAction;

    @BeforeEach
    void setUp() {
        fieldAction = new TestFieldAction();
    }

    @Test
    void testDoAction() {
        Board board = new Board(10, 10); // Assuming Board has a constructor with width and height
        GameController gameController = new GameController(board); // Pass the required argument
        Space space = new Space(board, 0, 0); // Pass the board and coordinates
        boolean result = fieldAction.doAction(gameController, space);
        assertTrue(result, "doAction should return true");
    }
}