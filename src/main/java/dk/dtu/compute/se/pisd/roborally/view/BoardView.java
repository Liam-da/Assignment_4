/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ekkart Kindler, ekki@dtu.dk
 */
/**
 * The BoardView class is responsible for displaying the game board and player information
 * in the RoboRally game. It updates the visual representation of the game based on changes
 * to the board and player movements. It also includes a mouse event handler to facilitate
 * manual player movement for testing purposes.
 *
 * This class implements the ViewObserver interface, allowing it to be updated when the
 * state of the board changes.
 */
public class BoardView extends VBox implements ViewObserver {

    /** The game board being displayed */
    private Board board;

    /** The main pane for displaying the board */
    private GridPane mainBoardPane;

    /** Array of SpaceView objects representing each space on the board */
    private SpaceView[][] spaces;

    /** The view for displaying players */
    private PlayersView playersView;

    /** Label for displaying the status message */
    private Label statusLabel;

    /** Event handler for handling clicks on spaces */
    private SpaceEventHandler spaceEventHandler;

    /**
     * Constructs a BoardView instance for the given game controller.
     *
     * @param gameController the GameController object controlling the game
     */

    public BoardView(@NotNull GameController gameController) {
        board = gameController.board;

        mainBoardPane = new GridPane();
        playersView = new PlayersView(gameController);
        statusLabel = new Label("<no status>");

        this.getChildren().add(mainBoardPane);
        this.getChildren().add(playersView);
        this.getChildren().add(statusLabel);

        spaces = new SpaceView[board.width][board.height];

        spaceEventHandler = new SpaceEventHandler(gameController);

        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Space space = board.getSpace(x, y);
                SpaceView spaceView = new SpaceView(space);
                spaces[x][y] = spaceView;
                mainBoardPane.add(spaceView, x, y);
                spaceView.setOnMouseClicked(spaceEventHandler);
            }
        }
        // Attach the view to the board so it can be updated
        board.attach(this);
        update(board);
    }

    /**
     * Updates the view when the state of the subject changes.
     *
     * @param subject the subject that has been updated
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == board) {
            //System.out.println("BoardView updated! Move Count: " + board.getMoveCount());

            // Update the status label with the board's status message
            statusLabel.setText(board.getStatusMessage());
        }
    }



   // XXX this handler and its uses should eventually be deleted! This is just to help test the
    //     behaviour of the game by being able to explicitly move the players on the board!
    private class SpaceEventHandler implements EventHandler<MouseEvent> {

       /** The game controller used to move players */
        final public GameController gameController;

       /**
        * Constructs a SpaceEventHandler with the given game controller.
        *
        * @param gameController the GameController object controlling the game
        */
        public SpaceEventHandler(@NotNull GameController gameController) {
            this.gameController = gameController;
        }

       /**
        * Handles mouse click events on spaces.
        *
        * @param event the mouse event triggered by clicking a space
        */
        @Override
        public void handle(MouseEvent event) {
            Object source = event.getSource();
            if (source instanceof SpaceView) {
                SpaceView spaceView = (SpaceView) source;
                Space space = spaceView.space;
                Board board = space.board;

                // Move the current player to the clicked space and increment the move count
                if (board == gameController.board) {
                    gameController.moveCurrentPlayerToSpace(space);
                    board.incrementMoveCount(); // Increment move count each time a player moves
                    event.consume();
                }
            }
        }


    }

}
