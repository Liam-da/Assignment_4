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
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.scene.control.TabPane;

/**
 * PlayersView is responsible for displaying the players' information
 * in the game. It extends the TabPane to manage multiple tabs for each player.
 * It observes changes in the Board and updates the view accordingly.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class PlayersView extends TabPane implements ViewObserver {

    private Board board; // reference to the game board.

    private PlayerView[] playerViews; // array of playerView for each player

    /**
     * Constructor initializes the PlayersView with a GameController.
     * It creates a tab for each player and adds it to the view.
     *
     * @param gameController the GameController controlling the game
     */
    public PlayersView(GameController gameController) {
        board = gameController.board;

        // disable tab closing
        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        // Initialize the playerViews array with the number of players
        playerViews = new PlayerView[board.getPlayersNumber()];

        // Create a PlayerView for each player and add it as a tab
        for (int i = 0; i < board.getPlayersNumber();  i++) {
            playerViews[i] = new PlayerView(gameController, board.getPlayer(i));
            this.getTabs().add(playerViews[i]);
        }

        // Attach this view as an observer to the board
        board.attach(this);

        // Initial update of the view
        update(board);
    }

    /**
     * This method is called when the observed subject (the board) is updated.
     * It selects the tab corresponding to the current player.
     *
     * @param subject the subject being observed (the board in this case)
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == board) {
            Player current = board.getCurrentPlayer();
            this.getSelectionModel().select(board.getPlayerNumber(current));
        }
    }

}
