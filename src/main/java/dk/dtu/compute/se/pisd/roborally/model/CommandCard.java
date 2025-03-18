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

/**
 * Represents a command card in the RoboRally game. A command card contains a single
 * command that a player can use to perform actions on their turn. The card is associated
 * with a specific command, and the player can execute the action associated with that command.
 * This class extends the {@link Subject} class, which is part of the observer pattern
 * allowing for state updates and notifications to be sent to observers when the card's state changes.
 *
 */
public class CommandCard extends Subject {

    /**
     * The command associated with this command card.
     */
    final public Command command;

    /**
     * Constructs a new CommandCard with the specified command.
     *
     * @param command the command to associate with this card
     */
    public CommandCard(@NotNull Command command) {
        this.command = command;
    }

    /**
     * Gets the name of the command associated with this command card.
     *
     * @return the display name of the command
     */
    public String getName() {
        return command.displayName;
    }


}
