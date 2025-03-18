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
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a space on the game board in RoboRally.
 * Each space has coordinates (x, y) and may contain a player,
 * walls in specific directions, and field actions.
 *
 * The class extends {@link Subject} to allow observers to be notified
 * when changes occur, such as a player moving into or out of the space.
 *
 * <p>Walls and field actions should only be modified during game setup,
 * not while the game is running.</p>
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Space extends Subject {

    public final Board board;
    public final int x;
    public final int y;
    private Player player;
    public ConveyorBelt conveyorBelt;

    public ConveyorBelt getConveyorBelt() {
        return conveyorBelt;
    }
    public void setConveyorBelt(ConveyorBelt conveyorBelt) {
        this.conveyorBelt = conveyorBelt;
    }

    // XXX A3
    private List<Heading> walls = new ArrayList<>();

    // XXX A3
    private List<FieldAction> actions = new ArrayList<>();

    /**
     * Constructs a new space at the given coordinates on the specified board.
     * Initially, the space does not contain a player.
     *
     * @param board The board this space belongs to.
     * @param x The x-coordinate of this space.
     * @param y The y-coordinate of this space.
     */
    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        player = null;
    }


    /**
     * Returns the player currently occupying this space.
     *
     * @return The player in this space, or null if unoccupied.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player occupying this space. If the player is moved to this space,
     * it updates the player's position and ensures consistency.
     *
     * @param player The player to set in this space, or null to clear the space.
     */
    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer && (player == null || board == player.board)) {
            this.player = player;
            if (oldPlayer != null) {
                // this should actually not happen
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifyChange();
        }
    }

    /**
     * Returns the walls (actually their direction) on this space.
     * Note that clients may change this list; this should, however,
     * be done only during the setup of the game (not while the game
     * is running).
     *
     * @return the list of walls on this space
     */
    // XXX A3
    public List<Heading> getWalls() {
        return walls;
    }

    /**
     * Adds a wall in the specified direction to this space.
     *
     * @param heading The direction in which the wall is added.
     */

    public void addWall(Heading heading) {
        System.out.println("AddWall" + heading);
        this.walls.add(heading);
    }

    /**
     * Checks if there is a wall in the specified direction on this space.
     *
     * @param heading The direction to check for a wall.
     * @return True if there is a wall in the given direction, false otherwise.
     */
    public void addAction(FieldAction action) {
        System.out.println("AddAction" + action);
        this.actions.add(action);
    }
    public boolean hasAction(Class<? extends FieldAction> actionClass) {
        for (FieldAction action : actions) {
            if (action.getClass() == actionClass) {
                return true;
            }
        }
        return false;
    }

    public boolean hasWall(Heading heading) {

        return walls.contains(heading);
    }

    /**
     * Returns the list of field actions on this space.
     * Note that clients may change this list; this should, however,
     * be done only during the setup of the game (not while the game
     * is running).
     *
     * @return the list of field actions on this space
     */

    public List<FieldAction> getActions() {
        return actions;
    }

    /**
     * Notifies observers that the player on this space has changed.
     * This is used when a player's attributes change and an update is needed.
     */
    void playerChanged() {
        // This is a minor hack; since some views that are registered with the space
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
        notifyChange();
    }


}
