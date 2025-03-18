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


import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a conveyor belt on a space.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
// XXX A3
public class ConveyorBelt extends FieldAction {

    private Heading heading;

    public ConveyorBelt() {
        super();
    }

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    /**
     * Implementation of the action of a conveyor belt. Needs to be implemented for A3.
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {

        // Implemented by Liam
        Player player = space.getPlayer(); // Get the player currently in this space
        if (player != null) {
            Heading conveyorDirection = this.getHeading(); // Get conveyor belt's direction
            Space targetSpace = gameController.board.getNeighbour(space, conveyorDirection); // Get the next space in conveyor direction

            if (targetSpace != null && targetSpace.getPlayer() == null) { // Move only if space is empty
                player.setSpace(targetSpace); // Move the player to the target space
                System.out.println("🚀 Player moved by Conveyor Belt to (" + targetSpace.x + ", " + targetSpace.y + ")");
                return true; // Successfully moved the player
            } else {
                System.out.println("⛔ Conveyor Belt blocked at (" + space.x + ", " + space.y + ")");
            }
        }
        return false;
    }
}
