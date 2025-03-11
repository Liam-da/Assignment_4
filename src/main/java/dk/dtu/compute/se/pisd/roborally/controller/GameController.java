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

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class GameController {

    final public Board board;

    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space) {

        // the method implemented
        Player currentPlayer = board.getCurrentPlayer(); // Get the current player

        if (space.getPlayer() == null) { // Check if the player exists and the space is free

          if(currentPlayer.getSpace() != null){
              currentPlayer.getSpace().setPlayer(null);
          }

          space.setPlayer(currentPlayer); // Set the player in the new space

          int nextPlayerIndex = (board.getPlayerNumber(currentPlayer) + 1) % board.getPlayersNumber();
          board.setCurrentPlayer(board.getPlayer(nextPlayerIndex));
        }
    }

    // XXX V2
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    // XXX V2
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    // XXX V2
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }

    // XXX V2
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    // XXX V2
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    // XXX V2
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    // XXX V2
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    // XXX V2
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    // XXX V2
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;
                    executeCommand(currentPlayer, command);
                }
                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    } else {
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }

    // XXX V2
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {
                case FORWARD:
                    this.moveForward(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD:
                    this.fastForward(player);
                    break;
                case U_TURN:
                    this.uTurn(player);
                    break;
                case BACKWARD:
                    this.backward(player);
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }

    // Implemented by Liam
    /*public void moveForward(@NotNull Player player) {
        // Get the current space of the player
        Space currentSpace = player.getSpace();
        if (currentSpace != null) {
            // Calculate the new coordinates for the player (moving up one space)
            int newX = currentSpace.x;
            int newY = currentSpace.y - 1; // Move up one space
            if (newY >= 0) {
                // Get the new space on the board
                Space newSpace = board.getSpace(newX, newY);
                // Check if the new space is free
                if (newSpace.getPlayer() == null) {
                    // Remove the player from the current space
                    currentSpace.setPlayer(null);
                    // Place the player in the new space
                    newSpace.setPlayer(player);
                    // Update the player's space reference
                    player.setSpace(newSpace);
                    // Increment the move count on the board
                    board.incrementMoveCount();
                }
            }
        }
    }*/

    public void moveForward(@NotNull Player player) {
        if (player.board == board) {
            Space space = player.getSpace();
            Heading heading = player.getHeading();

            Space target = board.getNeighbour(space, heading);

            if (target != null) {
                try {
                    moveToSpace(player, target, heading);
                } catch (ImpossibleMoveException e) {
                    //Don't do anything, other than catch exception
                }
            }
        }
    }

    public static class ImpossibleMoveException extends Throwable{
        public ImpossibleMoveException() {
        }
    }

    public void moveToSpace(@NotNull Player pusher, @NotNull Space space, @NotNull Heading heading) throws ImpossibleMoveException {
        assert board.getNeighbour(pusher.getSpace(), heading) == space;
        Player pushed = space.getPlayer();
        if (pushed != null) {
            Space nextSpace = board.getNeighbour(space, heading);
            if (nextSpace != null) {
                moveToSpace(pushed, nextSpace, heading);

                assert space.getPlayer() == null: "Space for the player is not free!";
            } else {
                throw new ImpossibleMoveException();
            }
        }
        pusher.setSpace(space);


    }

    // Implemented by Liam
    public void fastForward(@NotNull Player player) {
        // Move the player forward once
        moveForward(player);
        // Move the player forward again
        moveForward(player);
    }

    // Implemented by Liam
    public void turnRight(@NotNull Player player) {
        // Get the current direction of the player
        Heading currentDirection = player.getHeading();

        // Calculate the new direction (90 degrees clockwise)
        Heading newDirection = Heading.values()[(currentDirection.ordinal() + 1) % Heading.values().length];

        // Update the player's direction
        player.setHeading(newDirection);

    }

    // Implemented by Liam
    public void turnLeft(@NotNull Player player) {
        // Get the current direction of the player
        Heading currentDirection = player.getHeading();

        // Calculate the new direction (90 degrees counterclockwise)
        Heading newDirection = Heading.values()[(currentDirection.ordinal() + Heading.values().length - 1) % Heading.values().length];

        // Update the player's direction
        player.setHeading(newDirection);
    }


    // Implemented by Hannah
    public void uTurn(@NotNull Player player){
        // Get the current direction of the player
        Heading currentDirection = player.getHeading();

        // Calculate the new direction (180 degrees counterclockwise)
        Heading newDirection = Heading.values()[(currentDirection.ordinal() + Heading.values().length - 2) % Heading.values().length];

        // Update the player's direction
        player.setHeading(newDirection);
    }

    // Implemented by Hannah
    public void backward(@NotNull Player player) {
        // Get the current space of the player
        Space currentSpace = player.getSpace();
        if (currentSpace != null) {
            // Get the opposite heading of the player
            Heading oppositeHeading = player.getHeading().opposite();

            // Get the new space by moving in the opposite direction
            Space newSpace = board.getNeighbour(currentSpace, oppositeHeading);

            // Check if the new space is free
            if (newSpace != null && newSpace.getPlayer() == null) {
                // Move the player
                currentSpace.setPlayer(null);
                newSpace.setPlayer(player);
                player.setSpace(newSpace);
                board.incrementMoveCount();
            }
        }
    }



    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * A method called when no corresponding controller operation is implemented yet.
     * This should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }


}
