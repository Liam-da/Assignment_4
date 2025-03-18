package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardFactoryTest {

    private BoardFactory boardFactory;

    @BeforeEach
    void setUp() {
        boardFactory = BoardFactory.getInstance();
    }

    /**
     * Test that BoardFactory returns the singleton instance correctly.
     */
    @Test
    void testSingletonInstance() {
        BoardFactory anotherInstance = BoardFactory.getInstance();
        assertSame(boardFactory, anotherInstance, "BoardFactory should be a singleton.");
    }

    /**
     * Test that getAvailableBoardNames() returns correct board names.
     */
    @Test
    void testGetAvailableBoardNames() {
        List<String> boardNames = boardFactory.getAvailableBoardNames();
        assertNotNull(boardNames, "Board names list should not be null.");
        assertEquals(2, boardNames.size(), "BoardFactory should return exactly two board names.");
        assertTrue(boardNames.contains("Board1"), "Board1 should be in the available board names.");
        assertTrue(boardNames.contains("Board2"), "Board2 should be in the available board names.");
    }

    /**
     * Test creating Board1 and verifying its properties.
     */
    @Test
    void testCreateBoard1() {
        Board board1 = boardFactory.createBoard("Board1");
        assertNotNull(board1, "Board1 should not be null.");
        assertEquals("Board1", board1.getName(), "Board1 should have the correct name.");
        assertEquals(14, board1.getWidth(), "Board1 should have a width of 14.");
        assertEquals(8, board1.getHeight(), "Board1 should have a height of 8.");
    }

    /**
     * Test creating Board2 and verifying its properties.
     */
    @Test
    void testCreateBoard2() {
        Board board2 = boardFactory.createBoard("Board2");
        assertNotNull(board2, "Board2 should not be null.");
        assertEquals("Board2", board2.getName(), "Board2 should have the correct name.");
        assertEquals(14, board2.getWidth(), "Board2 should have a width of 14.");
        assertEquals(8, board2.getHeight(), "Board2 should have a height of 8.");
    }

    /**
     * Test creating a board with an invalid name and checking that it falls back to the default.
     */
    @Test
    void testCreateBoardWithInvalidName() {
        Board defaultBoard = boardFactory.createBoard("InvalidBoard");
        assertNotNull(defaultBoard, "Default board should not be null.");
        assertEquals("<none>", defaultBoard.getName(), "Default board should have the name '<none>'.");
        assertEquals(8, defaultBoard.getWidth(), "Default board should have a width of 8.");
        assertEquals(8, defaultBoard.getHeight(), "Default board should have a height of 8.");
    }
}