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

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

/**
 * Represents a player in the RoboRally game. A player has a name, a color,
 * a position on the board (space), a heading (direction), a set of program fields,
 * and a set of command cards.
 *
 * The player interacts with the game board and can change its attributes such as
 * position, heading, and checkpoint counter.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Player extends Subject {

    /** Number of registers available for the player */
    final public static int NO_REGISTERS = 5;
    /** Number of command cards available to the player */
    final public static int NO_CARDS = 8;

    /** The board to which the player belongs */
    final public Board board;

    private String name;
    private String color;
    private Space space;
    private Heading heading = SOUTH;
    private CommandCardField[] program;
    private CommandCardField[] cards;
    private int checkPointCounter = 1;

    /**
     * Constructs a new player with a given board, color, and name.
     *
     * @param board the board the player belongs to
     * @param color the color of the player
     * @param name the name of the player
     */
    public Player(@NotNull Board board, String color, @NotNull String name) {
        this.board = board;
        this.name = name;
        this.color = color;

        this.space = null;

        program = new CommandCardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
        }

        cards = new CommandCardField[NO_CARDS];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CommandCardField(this);
        }
    }

    /**
     * Gets the player's name.
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the player's name and notifies any observers of the change.
     *
     * @param name the new name of the player
     */
    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            notifyChange();
            if (space != null) {
                space.playerChanged();

            }
        }
    }

    /**
     * Gets the player's color.
     *
     * @return the color of the player
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the player's color and notifies observers of the change.
     *
     * @param color the new color of the player
     */
    public void setColor(String color) {
        this.color = color;
        notifyChange();
        if (space != null) {
            space.playerChanged();
        }
    }

    /**
     * Gets the space the player is currently occupying.
     *
     * @return the space the player is in
     */
    public Space getSpace() {
        return space;
    }

    /**
     * Moves the player to a new space and updates the previous space accordingly.
     *
     * @param space the new space for the player
     */
    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace &&
                (space == null || space.board == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setPlayer(null);
            }
            if (space != null) {
                space.setPlayer(this);
            }
            notifyChange();
        }
    }

    /**
     * Gets the player's heading (direction).
     *
     * @return the player's current heading
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * Sets the player's heading and notifies observers of the change.
     *
     * @param heading the new heading of the player
     */
    public void setHeading(@NotNull Heading heading) {
        if (heading != this.heading) {
            this.heading = heading;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     * Sets the checkpoint counter for the player.
     *
     * @param checkPointCounter the new checkpoint count
     */
    public void setCheckPointCounter(int checkPointCounter) {
        this.checkPointCounter = checkPointCounter;
        notifyChange();


    }

    /**
     * Gets the checkpoint counter value.
     *
     * @return the checkpoint counter
     */
    public int getCheckPointCounter() {
        return checkPointCounter;
    }

    /**
     * Gets the program field at the specified index.
     *
     * @param i the index of the program field
     * @return the program field at the given index
     */
    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    /**
     * Gets the command card field at the specified index.
     *
     * @param i the index of the command card field
     * @return the command card field at the given index
     */
    public CommandCardField getCardField(int i) {
        return cards[i];
    }

}
