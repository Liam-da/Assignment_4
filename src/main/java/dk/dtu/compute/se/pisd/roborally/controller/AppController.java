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
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;



/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class AppController implements Observer {

    /**
     * List of available player number options.
     */
    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);

    /**
     * List of available player colors.
     */
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");

    final private RoboRally roboRally;

    private GameController gameController;

    /**
     * Singleton instance of BoardFactory to create game boards.
     */
    private BoardFactory boardFactory = BoardFactory.getInstance(); // Singleton instance

    /**
     * Constructor that initializes the AppController with the provided RoboRally application.
     *
     * @param roboRally The RoboRally application instance.
     */
    public AppController(@NotNull RoboRally roboRally) {

        this.roboRally = roboRally;
    }

    /**
     * Displays a dialog for selecting a game board.
     *
     * @return The name of the selected board.
     */
    private String showBoardSelectionDialog() {
        List<String> boardOptions = BoardFactory.getInstance().getAvailableBoardNames();

        ChoiceDialog<String> dialog = new ChoiceDialog<>(boardOptions.get(0), boardOptions);
        dialog.setTitle("Board Selection");
        dialog.setHeaderText("Select a board");
        dialog.setContentText("Choose a board:");

        Optional<String> result = dialog.showAndWait();
        return result.orElse(boardOptions.get(0));
    }

    /**
     * Starts a new game by prompting the user for board and player configurations.
     * Initializes game controller, creates players, and starts the programming phase.
     */
    public void newGame() {
        String selectedBoard = showBoardSelectionDialog();
        Board board = boardFactory.createBoard(selectedBoard);


        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.getFirst(), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent()) {
            int numberOfPlayers = result.get();

            gameController = new GameController(board);


            for (int i = 0; i < numberOfPlayers; i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                player.setName("Player " + (i + 1));
                board.addPlayer(player);
                player.setSpace(board.getSpace(i,i));

            }

            if (board.getPlayersNumber() > 0) {
                board.setCurrentPlayer(board.getPlayer(0));
            }

            gameController.startProgrammingPhase();


            //Platform.runLater(() ->
                    roboRally.createBoardView(gameController);
        }
    }


    /**
     * Saves the current game state to a file.
     * The saved game data includes the board and its state.
     */

    public void saveGame() {

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("roborally_save.dat"))) {
            out.writeObject(gameController.board);
            System.out.println("RoboRally game saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving RoboRally game: " + e.getMessage());
        }
    }

    /**
     * Loads a previously saved game state from a file.
     * The game data is deserialized and used to restore the game state.
     * If loading fails, a new game is started.
     */
    public void loadGame() {
        // Implemented by Liam


        // Implemented by Liam
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("roborally_save.dat"))) {
            Board loadedBoard = (Board) in.readObject(); // Deserialize the Board object
            gameController = new GameController(loadedBoard); // Initialize GameController with the loaded board
            System.out.println("RoboRally game loaded successfully.");

            // Update UI with the loaded game
            roboRally.createBoardView(gameController);

        } catch (IOException e) {
            System.err.println("Error loading RoboRally game (IO issue): " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading RoboRally game (Class issue): " + e.getMessage());
        }

        // If loading failed and gameController is null, start a new game as a fallback
        if (gameController == null) {
            System.out.println("Loading failed, starting a new game...");
            newGame();
        }
    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */

    //Saves the game
    //Removes the gameController, effectively stopping the game
    //Updates the UI to reflect that no game is running.
    public boolean stopGame() {
        if (gameController != null) {

            // here we save the game (without asking the user).
            saveGame();

            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }

    /**
     * Exits the application, prompting the user for confirmation if a game is running.
     * If the user chooses to exit, the application will shut down.
     */
    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isEmpty() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    /**
     * Checks whether a game is currently running.
     *
     * @return true if a game is running, false otherwise.
     */
    public boolean isGameRunning() {
        return gameController != null;
    }

    /**
     * Updates the observer with changes to the observed subject.
     * In this implementation, no action is taken for now.
     *
     * @param subject The observed subject.
     */
    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

}
