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
 * This represents a RoboRally game board. Which gives access to
 * all the information of current state of the games. Note that
 * the terms board and game are used almost interchangeably.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Board extends Subject {

    public final int width;

    public final int height;

    public final String boardName;

    private Integer gameId;

    private final Space[][] spaces;

    private final List<Player> players = new ArrayList<>();

    private Player current;

    private Phase phase = INITIALISATION;

    private int step = 0;

    private boolean stepMode;
    private int moveCount;

    /**
     * The board is represented of spaces that is a 2D array of space object.
     *
     *
     * @param width the numbers of columns in the board
     * @param height the numbers of rows in the board
     * @param boardName the given board name
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
    }

    /**
     *The boards size is determent by the width and the height.
     * @param width
     * @param height
     */
    public Board(int width, int height) {
        this(width, height, "defaultboard");
    }

    /**
     * Gets the game id, and returns it.
     * @return
     */
    public Integer getGameId() {
        return gameId;
    }

    /**
     * Sets the game id. If the set game id is null it will assign the new game id, else if the game id is already set,
     * the method checks if the new value is different.
     * If different, it throws an IllegalStateException to prevent modification.
     *
     * @param gameId the given game id
     * @throws IllegalStateException to prevent modification
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

    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width &&
                y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    /**
     * Gets the players number, and returns it.
     * @return
     */
    public int getPlayersNumber() {
        return players.size();
    }

    public void addPlayer(@NotNull Player player) {
        if (player.board == this && !players.contains(player)) {
            players.add(player);
            notifyChange();
        }
    }

    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }

    /**
     * Gets the current player's turn, and returns it.
     * @return
     */
    public Player getCurrentPlayer() {
        return current;
    }

    /**
     * Changes the current player and notifies.
     *
     * @param player the given player
     */
    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    /**
     * Tracks step-by-step execution
     * @return
     */
    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }

    public boolean isStepMode() {
        return stepMode;
    }

    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }

    public int getPlayerNumber(@NotNull Player player) {
        if (player.board == this) {
            return players.indexOf(player);
        } else {
            return -1;
        }
    }

    /**
     * Returns the neighbour of the given space of the board in the given heading.
     * The neighbour is returned only, if it can be reached from the given space
     * (no walls or obstacles in either of the involved spaces); otherwise,
     * null will be returned.
     *
     * @param space the space for which the neighbour should be computed
     * @param heading the heading of the neighbour
     * @return the space in the given direction; null if there is no (reachable) neighbour
     */
    public Space getNeighbour(@NotNull Space space, @NotNull Heading heading) {
        // Implemented by Liam
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

    public String getStatusMessage() {
        // this is actually a view aspect, but for making assignment V1 easy for
        // the students, this method gives a string representation of the current
        // status of the game

        // Implemented by Liam
        return "Player = " + getCurrentPlayer().getName() + ", Move Count = " + getMoveCount() + ", Checkpoint = " + getCurrentPlayer().getCheckPointCounter();

    }
    public void incrementMoveCount() {
        moveCount++;
        System.out.println("Move Count updated: " + moveCount + " on " + this);
        notifyChange();

    }

    public int getMoveCount() {
        System.out.println("getMoveCount() called, value: " + moveCount);  // ✅ Debug print
        return moveCount;
    }
    public void updateBoard(){
        System.out.println("🔄 Board UI opdateres!");

        notifyChange();
    }

}
