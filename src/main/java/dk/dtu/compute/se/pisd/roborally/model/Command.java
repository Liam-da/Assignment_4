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

/**
 * Enum representing different commands that a player can execute in the RoboRally game.
 * Each command has a display name that describes its action.
 * The enum provides a simple way of representing various movement and action commands.
 * This enum includes commands like moving forward, turning, fast forward, U-turn, and more.
 * The commands are as follows:
 * - FORWARD: Move forward one space.
 * - RIGHT: Turn right.
 * - LEFT: Turn left.
 * - FAST_FORWARD: Move forward with a higher speed.
 * - U_TURN: Make a U-turn (turn 180 degrees).
 * - BACKWARD: Move backward one space.
 * - LEFT_OR_RIGHT: A command that allows the player to choose between turning left or right.
 *
 */
public enum Command {

    // This is a very simplistic way of realizing different commands.
    /**
     * Move forward one space.
     */
    FORWARD("Fwd"),
    /**
     * Turn to the right.
     */
    RIGHT("Turn Right"),
    /**
     * Turn to the left.
     */
    LEFT("Turn Left"),
    /**
     * Move forward with a higher speed.
     */
    FAST_FORWARD("Fast Fwd"),
    /**
     * Make a U-turn (180-degree turn).
     */    U_TURN("U Turn"),
    /**
     * Move backward one space.
     */
    BACKWARD("Backward"),
    /**
     * A command that allows the player to choose between turning left or right.
     */
    LEFT_OR_RIGHT("Left or Right");

    /**
     * The display name of the command that describes the action.
     */
    final public String displayName;

    /**
     * Constructor to initialize the display name of the command.
     *
     * @param displayName the display name for the command
     */
    Command(String displayName) {
        this.displayName = displayName;
    }

}
