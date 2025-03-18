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

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * Represents the menu bar for the RoboRally game.
 *
 * The menu bar contains options such as starting a new game, stopping the current game,
 * saving the game, loading a game, and exiting the application. The visibility of these options
 * is dynamically updated based on whether a game is running or not.
 *
 * This class extends the {@link MenuBar} class and is used in the RoboRally game's graphical user interface.
 * It interacts with the {@link AppController} to trigger actions based on user input.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */

public class RoboRallyMenuBar extends MenuBar {

    private AppController appController;

    private Menu controlMenu;

    private MenuItem saveGame;

    private MenuItem newGame;

    private MenuItem loadGame;

    private MenuItem stopGame;

    private MenuItem exitApp;

    /**
     * Constructs a new RoboRallyMenuBar with the given AppController.
     *
     * The constructor sets up the menu options for the game, associating actions with each menu item
     * and updating the visibility of the items depending on the current game state.
     *
     * @param appController The controller that handles the game logic, including starting,
     *                      stopping, saving, and loading the game.
     */

    public RoboRallyMenuBar(AppController appController) {
        this.appController = appController;

        controlMenu = new Menu("File");
        this.getMenus().add(controlMenu);

        // New Game Menu Item
        newGame = new MenuItem("New Game");
        newGame.setOnAction( e -> this.appController.newGame());
        controlMenu.getItems().add(newGame);

        // Stop Game Menu Item
        stopGame = new MenuItem("Stop Game");
        stopGame.setOnAction( e -> this.appController.stopGame());
        controlMenu.getItems().add(stopGame);

        // Save Game Menu Item
        saveGame = new MenuItem("Save Game");
        saveGame.setOnAction( e -> this.appController.saveGame());
        controlMenu.getItems().add(saveGame);

        // Load Game Menu Item
        loadGame = new MenuItem("Load Game");
        loadGame.setOnAction( e -> this.appController.loadGame());
        controlMenu.getItems().add(loadGame);

        // Exit Application Menu Item
        exitApp = new MenuItem("Exit");
        exitApp.setOnAction( e -> this.appController.exit());
        controlMenu.getItems().add(exitApp);

        // Update menu visibility based on game state
        controlMenu.setOnShowing(e -> update());
        controlMenu.setOnShown(e -> this.updateBounds());

        // Initial update of menu visibility
        update();
    }

    /**
     * Updates the visibility of menu items based on whether the game is running or not.
     *
     * If the game is running, it hides the "New Game" and "Load Game" options while showing
     * the "Stop Game" and "Save Game" options. If the game is not running, it hides the
     * "Stop Game" and "Save Game" options and shows the "New Game" and "Load Game" options.
     */
    public void update() {
        if (appController.isGameRunning()) {
            newGame.setVisible(false);
            stopGame.setVisible(true);
            saveGame.setVisible(true);
            loadGame.setVisible(false);
        } else {
            newGame.setVisible(true);
            stopGame.setVisible(false);
            saveGame.setVisible(false);
            loadGame.setVisible(true);
        }
    }

}
