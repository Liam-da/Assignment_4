package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.CheckPoint;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import java.util.List;

/**
 * A factory for creating boards. The factory itself is implemented as a singleton.
 *
 * @author Ekkart Kindler, modified by [DIT NAVN]
 */
public class BoardFactory {

    /**
     * The single instance of this class, which is lazily instantiated on demand.
     */
    private static BoardFactory instance = null;

    /**
     * Constructor for BoardFactory. It is private in order to make the factory a singleton.
     */
    private BoardFactory() {
    }

    /**
     * Returns the singleton instance of BoardFactory.
     *
     * @return the single instance of the BoardFactory
     */
    public static BoardFactory getInstance() {
        if (instance == null) {
            instance = new BoardFactory();
        }
        return instance;
    }

    /**
     * Returns a list of available board names.
     *
     * @return List of available board names.
     */
    public List<String> getAvailableBoardNames() {

        return List.of("Board1", "Board2");
    }

    /**
     * Creates a new board based on the given name.
     *
     * @param name the given board name
     * @return the new board corresponding to that name
     */
    public Board createBoard(String name) {
        Board board;

        if ("Board1".equals(name)) {
            board = new Board(8, 8, "Board1");
            setupBoard1(board);
        } else if ("Board2".equals(name)) {
            board = new Board(14, 8, "Board2");
            setupBoard2(board);
        } else {
            board = new Board(8, 8, "<none>");
            setupBoard1(board);
        }
        board.updateBoard();
        return board;
    }

    private void setupBoard1(Board board) {
        addWall(board, 0, 0, Heading.SOUTH);
        addWall(board, 1, 0, Heading.NORTH);
        addWall(board, 1, 1, Heading.WEST);

        addCheckpoint(board, 4, 0, 1);
        addCheckpoint(board, 6, 6, 2);
        addCheckpoint(board, 2, 6, 3);
    }

    private void setupBoard2(Board board) {

        addWall(board, 4, 4, Heading.EAST);
        addWall(board, 6, 6, Heading.WEST);
        addWall(board, 8, 8, Heading.SOUTH);
        addWall(board, 10, 10, Heading.NORTH);


        addCheckpoint(board, 1, 1, 1);
        addCheckpoint(board, 5, 4, 2);
        addCheckpoint(board, 7, 3, 3);
    }

    private void addWall(Board board, int x, int y, Heading heading) {
        Space space = board.getSpace(x, y);
        if (space != null) {
            space.addWall(heading);
        }
    }

    private void addCheckpoint(Board board, int x, int y, int checkpointNumber) {
        Space space = board.getSpace(x, y);
        if (space != null) {
            space.getActions().add(new CheckPoint(checkpointNumber));
        }
    }
}
