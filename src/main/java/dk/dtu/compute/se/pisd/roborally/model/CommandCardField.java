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

/**
 * Represents a field that holds a command card for a player in the RoboRally game.
 * This class extends {@link Subject} to support the observer pattern, allowing
 * notifications when the command card or its visibility changes.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class CommandCardField extends Subject {

    /** The player who owns this command card field. */
    final public Player player;

    /** The command card currently assigned to this field. */
    private CommandCard card;

    /** Indicates whether the command card is visible to other players. */
    private boolean visible;

    /**
     * Constructs a CommandCardField for a specific player.
     *
     * @param player the player who owns this command card field.
     */
    public CommandCardField(Player player) {
        this.player = player;
        this. card = null;
        this.visible = true;
    }

    /**
     * Retrieves the command card currently assigned to this field.
     *
     * @return the assigned command card, or null if no card is assigned.
     */
    public CommandCard getCard() {
        return card;
    }


    /**
     * Sets a new command card for this field.
     *
     * @param card the new command card to assign.
     */
    public void setCard(CommandCard card) {
        if (card != this.card) {
            this.card = card;
            notifyChange();
        }
    }

    /**
     * Checks whether the command card is visible to other players.
     *
     * @return true if the card is visible, false otherwise.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the visibility of the command card.
     *
     * @param visible true to make the card visible, false to hide it.
     */
    public void setVisible(boolean visible) {
        if (visible != this.visible) {
            this.visible = visible;
            notifyChange();
        }
    }
}
