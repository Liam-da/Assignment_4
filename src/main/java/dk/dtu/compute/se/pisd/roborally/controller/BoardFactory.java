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
    private BoardFactory() {}

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

        if (name == null) {
            board = new Board(8, 8, "<none>");
        } else if (name.equals("Board1")) {
            board = new Board(8, 8, "Board1");
        } else if (name.equals("Board2")) {
            board = new Board(8, 8, "Board2");
        } else {
            board = new Board(8, 8, "<none>");
        }

        /**
         * Adds walls, actions, and checkpoints to the board spaces.
         * The configuration is different for each board type.
         */
        Space space = board.getSpace(0, 0);
        space.getWalls().add(Heading.SOUTH);
        // Fjernet ConveyorBelt, hvis den ikke er implementeret endnu
        // ConveyorBelt action = new ConveyorBelt();
        // action.setHeading(Heading.WEST);
        // space.getActions().add(action);

        space = board.getSpace(1, 0);
        space.getWalls().add(Heading.NORTH);

        space = board.getSpace(1, 1);
        space.getWalls().add(Heading.WEST);

        space = board.getSpace(5, 5);
        space.getWalls().add(Heading.SOUTH);

        space = board.getSpace(6, 5);
        space.getWalls().add(Heading.WEST);

        /**
         * Adding checkpoints to specific locations on the board.
         */
        space = board.getSpace(4, 0);
        CheckPoint checkpoint = new CheckPoint(1);
        space.getActions().add(checkpoint);

        space = board.getSpace(6, 6);
        CheckPoint checkpoint2 = new CheckPoint(2);
        space.getActions().add(checkpoint2);

        space = board.getSpace(2,6);
        CheckPoint checkpoint3 = new CheckPoint(3);
        space.getActions().add(checkpoint3);

        return board;
    }
}
