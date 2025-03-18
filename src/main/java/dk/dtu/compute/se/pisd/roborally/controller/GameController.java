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
import dk.dtu.compute.se.pisd.roborally.model.Command;

import java.util.List;


/**
 * The GameController class is responsible for managing the game flow,
 * handling player movements, executing commands, and controlling different
 * phases of the game.
 *
 * This class interacts with the Board and Player objects to ensure
 * correct game mechanics.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */

public class GameController {

    final public Board board;

    /**
     * Constructs a GameController with the specified board.
     *
     * @param board The game board to be controlled.
     */
    public GameController(@NotNull Board board) {
        this.board = board;
        System.out.println("GameController initialized with Board: " + board);


    }

    /**
     * Moves the game to the next step, switching players and updating the phase if necessary.
     *
     * @param currentPlayer The player currently taking action.
     * @param step          The current step in the phase.
     */
    private void advanceToNextStep(Player currentPlayer, int step) {
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
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space) {
        Player currentPlayer = board.getCurrentPlayer(); // Get the current player
        if (space.getPlayer() == null) { // Check if the player exists and the space is free
            if (currentPlayer.getSpace() != null) {
                currentPlayer.getSpace().setPlayer(null);
            }

            currentPlayer.setSpace(space);
            space.setPlayer(currentPlayer); // Set the player in the new space

            int nextPlayerIndex = (board.getPlayerNumber(currentPlayer) + 1) % board.getPlayersNumber();
            board.setCurrentPlayer(board.getPlayer(nextPlayerIndex));
        }
    }

    /**
     * Starts the programming phase where players receive and program command cards.
     */
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

    /**
     * Generates a random command card for players.
     *
     * @return A randomly selected CommandCard.
     */
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    /**
     * Executes the next step in the current phase of the game.
     */
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

    /**
     * Executes the next step in the current phase of the game.
     */
    private void executeNextStep() {
        //System.out.println("Executing next step. Current Phase: " + board.getPhase());
        if (board.getPhase() == Phase.FINISHED) {
            System.out.println("The game is over");
            return;
        }

        if (board.getPhase() == Phase.PLAYER_INTERACTION) {
            System.out.println("Player interaction complete. Resuming activation phase.");
            board.setPhase(Phase.ACTIVATION);
            executeNextStep(); // Resume activation phase
            return;
        }
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;
                    if (command == Command.LEFT_OR_RIGHT) {
                        board.setPhase(Phase.PLAYER_INTERACTION);
                        return; // Wait for player input
                    }
                    executeCommand(currentPlayer, command);
                }
                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    for (int i = 0; i < board.getPlayersNumber(); i++) {
                        Player player = board.getPlayer(i);
                        List<FieldAction> actions = player.getSpace().getActions();
                        for (int j = 0; j < actions.size(); j++) {
                            actions.get(j).doAction(this, player.getSpace());
                        }
                    }

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
    public void playerTurnChoice(int direction) {
        Player currentPlayer = board.getCurrentPlayer();
        if (direction == -1) {
            turnLeft(currentPlayer);
        } else {
            turnRight(currentPlayer);
        }
        board.setPhase(Phase.ACTIVATION);
        //executeNextStep();
        //continuePrograms();
        advanceToNextStep(currentPlayer, board.getStep());
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

    /**
     * Moves a player forward in their current heading direction.
     *
     * @param player The player to move forward.
     * @return true if the player successfully moved forward, false otherwise.
     */
    public boolean moveForward(@NotNull Player player) {
        if (player.board == board) {
            Space space = player.getSpace();
            Heading heading = player.getHeading();

            Space target = board.getNeighbour(space, heading);

            if (target != null) {
                try {
                    moveToSpace(player, target, heading);
                    board.incrementMoveCount();
                    return true;
                    //player moved forward
                } catch (ImpossibleMoveException e) {
                    //Don't do anything, other than catch exception
                    return false;
                    //movement failed
                }
            }
        }
        return false;
    }

    /**
     * Exception indicating an impossible move.
     */
    public static class ImpossibleMoveException extends Exception {
        public ImpossibleMoveException() {
        }
    }


    /**
     * Moves a player to a specified space in a given direction, handling potential collisions.
     *
     * @param pusher  The player attempting to move.
     * @param space   The target space to move to.
     * @param heading The direction of movement.
     * @throws ImpossibleMoveException if the move is not possible.
     */
    public void moveToSpace(@NotNull Player pusher, @NotNull Space space, @NotNull Heading heading) throws ImpossibleMoveException {
        assert board.getNeighbour(pusher.getSpace(), heading) == space;
        Player pushed = space.getPlayer();
        if (pushed != null) {
            Space nextSpace = board.getNeighbour(space, heading);
            if (nextSpace != null) {
                moveToSpace(pushed, nextSpace, heading);

                assert space.getPlayer() == null : "Space for the player is not free!";
            } else {
                throw new ImpossibleMoveException();
            }
        }
        pusher.setSpace(space);
        space.setPlayer(pusher);
    }

    private void rotate(@NotNull Player player, int steps) {
        Heading currentDirection = player.getHeading();
        Heading newDirection = Heading.values()[(currentDirection.ordinal() + steps + Heading.values().length) % Heading.values().length];
        player.setHeading(newDirection);
    }

    public void turnRight(@NotNull Player player) {
        rotate(player, 1);
    }

    public void turnLeft(@NotNull Player player) {
        rotate(player, -1);
    }

    public void fastForward(@NotNull Player player) {
        if (moveForward(player)) {
            moveForward(player);
        }
    }

    // Implemented by Hannah
    public void uTurn(@NotNull Player player) {
        // Get the current direction of the player
        Heading currentDirection = player.getHeading();

        // Calculate the new direction (180 degrees counterclockwise)
        Heading newDirection = Heading.values()[(currentDirection.ordinal() + Heading.values().length - 2) % Heading.values().length];

        // Update the player's direction
        player.setHeading(newDirection);
    }

    // Implemented by Leon
    public boolean backward(@NotNull Player player) {
        if (player == null || player.getSpace() == null) return false; // Sikrer mod fejl

        Space currentSpace = player.getSpace();
        Heading oppositeHeading = player.getHeading().opposite();
        Space newSpace = board.getNeighbour(currentSpace, oppositeHeading);


        if (newSpace == null) {
            System.out.println(" Player cannot move backward beyond the board edge!");
            return false;
        }

        if (newSpace.getPlayer() == null) {
            currentSpace.setPlayer(null);
            newSpace.setPlayer(player);
            player.setSpace(newSpace);
            board.incrementMoveCount();
            return true;
        }

        return false;
    }
}







