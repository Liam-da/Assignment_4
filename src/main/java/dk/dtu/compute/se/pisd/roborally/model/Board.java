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
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.INITIALISATION;

/**
 * Represents a RoboRally game board. The board provides access to all information
 * related to the current state of the game. The terms "board" and "game" are used
 * interchangeably in this context.
 * This class is responsible for managing the grid of spaces, handling the players,
 * tracking the current player, and maintaining the game phase and step-wise progression.
 * It also provides methods for managing player movement, game status, and notifications.
 *
 */
public class Board extends Subject {
    /**
     * The number of columns in the board.
     */
    public final int width;
    /**
     * The number of rows in the board.
     */
    public final int height;
    /**
     * The name of the board.
     */
    public final String boardName;
    /**
     * The unique identifier for the game.
     */
    private Integer gameId;
    /**
     * The 2D array of spaces that make up the board.
     */
    private final Space[][] spaces;
    /**
     * A list of players in the game.
     */
    private final List<Player> players = new ArrayList<>();
    /**
     * The current player in the game.
     */
    private Player current;
    /**
     * The current phase of the game.
     */
    private Phase phase = INITIALISATION;
    /**
     * The current step of the game.
     */
    private int step = 0;
    /**
     * A flag indicating whether the game is in step-by-step mode.
     */
    private boolean stepMode;
    /**
     * The number of moves made so far in the game.
     */
    private int moveCount;

    /**
     * Constructs a new game board with the specified dimensions and name.
     * Initializes all spaces on the board.
     *
     * @param width the number of columns in the board
     * @param height the number of rows in the board
     * @param boardName the name of the board
     */
    public Board(int width, int height, @NotNull String boardName) {
        this.boardName = boardName;
        this.width = width;
        this.height = height;
        spaces = new Space[width][height];

        System.out.println("New Board created: " + this);  // Debug print

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                spaces[x][y] = new Space(this, x, y);
            }
        }

        int[][] conveyorBeltPositions = {{2, 3, Heading.EAST.ordinal()}, {4, 5, Heading.NORTH.ordinal()}, {6, 2, Heading.WEST.ordinal()}, {1, 7, Heading.SOUTH.ordinal()}, {8, 4, Heading.EAST.ordinal()}};
        for (int[] pos : conveyorBeltPositions) {
            int x = pos[0];
            int y = pos[1];
            Heading heading = Heading.values()[pos[2]];

            if (spaces[x][y] != null) {
                ConveyorBelt conveyorBelt = new ConveyorBelt();
                conveyorBelt.setHeading(heading);
                spaces[x][y].addAction(conveyorBelt);
                System.out.println("Conveyor Belt Added at (" + x + ", " + y + ") → " + heading);
            } else {
                System.err.println("Error: Attempted to add conveyor belt to uninitialized space at (" + x + ", " + y + ")");

            }
        }
    }

    /**
     * Constructs a new game board with the specified dimensions and a default name.
     *
     * @param width the number of columns in the board
     * @param height the number of rows in the board
     */
    public Board(int width, int height) {
        this(width, height, "defaultboard");
    }

    /**
     * Gets the unique game ID.
     *
     * @return the current game ID
     */
    public Integer getGameId() {
        return gameId;
    }

    /**
     * Sets the game ID. If the game ID is already set, it throws an IllegalStateException
     * if the new value is different.
     *
     * @param gameId the new game ID
     * @throws IllegalStateException if the game ID is already set to a different value
     */
    public void setGameId(int gameId) {
        if (this.gameId == null) {
            this.gameId = gameId;
        } else {
            if (!this.gameId.equals(gameId)) {
                throw new IllegalStateException("A game with a set id may not be assigned a new id!");
            }
        }
    }

    /**
     * Gets the space at the specified coordinates on the board.
     *
     * @param x the x-coordinate (column index) of the space
     * @param y the y-coordinate (row index) of the space
     * @return the space at the given coordinates, or null if out of bounds
     */
    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    /**
     * Gets the number of players currently in the game.
     *
     * @return the number of players
     */
    public int getPlayersNumber() {
        return players.size();
    }

    /**
     * Adds a player to the game, ensuring the player is not already added.
     * Notifies all observers of the change.
     *
     * @param player the player to add
     */
    public void addPlayer(@NotNull Player player) {
        if (player.board == this && !players.contains(player)) {
            players.add(player);
            notifyChange();
        }
    }

    /**
     * Gets the player at the specified index.
     *
     * @param i the index of the player in the list
     * @return the player at the given index, or null if out of bounds
     */
    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }

    /**
     * Gets the current player in the game.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return current;
    }

    /**
     * Sets the current player and notifies observers of the change.
     *
     * @param player the new current player
     */
    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    /**
     * Gets the current phase of the game.
     *
     * @return the current game phase
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * Checks if the new phase (phase) is different from the current phase (this.phase).
     * If the phase is the same as the current one, it skips the update.
     * But if the phase is different, the phase is updated to the new value (phase).
     * After updating the phase, notifyChange() is called to handle any necessary updates.
     * @param phase the new phase
     */
    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }

    /**
     * Gets the current step of the game.
     *
     * @return the current step
     */
    public int getStep() {
        return step;
    }

    /**
     * Sets the current step of the game and notifies observers if the step changes.
     *
     * @param step the new step value
     */
    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }
    /**
     * Checks if the game is in step-by-step mode.
     *
     * @return true if the game is in step mode, false otherwise
     */
    public boolean isStepMode() {
        return stepMode;
    }
    /**
     * Sets the step mode for the game and notifies observers if the mode changes.
     *
     * @param stepMode the new step mode state
     */
    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }
    /**
     * Gets the index of a given player in the list of players.
     *
     * @param player the player whose index is to be returned
     * @return the index of the player in the list, or -1 if the player is not found
     */
    public int getPlayerNumber(@NotNull Player player) {
        if (player.board == this) {
            return players.indexOf(player);
        } else {
            return -1;
        }
    }

    /**
     * Returns the neighboring space in the given direction (heading) from the provided space.
     * The neighbor is returned only if there are no walls or obstacles blocking the way.
     *
     * @param space the space for which the neighbor is to be calculated
     * @param heading the direction in which to find the neighboring space
     * @return the neighboring space, or null if no valid neighbor exists
     */

    public Space getNeighbour(@NotNull Space space, @NotNull Heading heading) {
        int x = space.x;
        int y = space.y;
        // Check if there's a wall in the given direction before calculating the neighbor
        List<Heading> walls = space.getWalls();
        if (walls.contains(heading)) {
            return null; // No neighbor in this direction due to a wall
        }
        switch (heading) {
            case SOUTH:
                y = (y + 1) % height;
                break;
            case WEST:
                x = (x + width - 1) % width;
                break;
            case NORTH:
                y = (y + height - 1) % height;
                break;
            case EAST:
                x = (x + 1) % width;
                break;
        }

        return getSpace(x, y);
    }
    /**
     * Returns a string representation of the current status of the game, including the current player,
     * the move count, and the checkpoint of the current player.
     *
     * @return a string representing the current game status
     */
    public String getStatusMessage() {
        // this is actually a view aspect, but for making assignment V1 easy for
        // the students, this method gives a string representation of the current
        // status of the game

        return "Player = " + getCurrentPlayer().getName() + ", Move Count = " + getMoveCount() + ", Checkpoint = " + getCurrentPlayer().getCheckPointCounter();

    }

    /**
     * Increments the move count by 1 and notifies observers of the change.
     */
    public void incrementMoveCount() {
        moveCount++;
        System.out.println("Move Count updated: " + moveCount + " on " + this);
        notifyChange();

    }
    /**
     * Gets the current move count.
     *
     * @return the current move count
     */
    public int getMoveCount() {
        System.out.println("getMoveCount() called, value: " + moveCount);  // ✅ Debug print
        return moveCount;
    }

        /**
         * Updates the board and notifies observers of the change.
         *
         */
        public void updateBoard() {
            System.out.println("🔄 Board UI opdateres!");

            notifyChange();
        }
    }

