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
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class GameController {

    final public Board board;

    public GameController(@NotNull Board board) {
        this.board = board;
        System.out.println("GameController initialized with Board: " + board);


    }

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
        //System.out.println("Executing next step. Current Phase: " + board.getPhase());
        if(board.getPhase() == Phase.FINISHED){
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
                    for(int i = 0; i < board.getPlayersNumber(); i++) {
                       Player player = board.getPlayer(i);
                       List<FieldAction> actions = player.getSpace().getActions();
                       for(int j = 0; j < actions.size(); j++) {
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
/*
    // Implemented by Liam
    public boolean moveForward(@NotNull Player player) {
        Space currentSpace = player.getSpace();
        if (currentSpace != null) {
            Heading heading = player.getHeading();
            Space nextSpace = board.getNeighbour(currentSpace, heading);

            System.out.println(player.getName() + " forsøger at rykke frem fra " + currentSpace.x + "," + currentSpace.y);

            if (nextSpace != null) {
                if (currentSpace.hasWall(heading) || nextSpace.hasWall(heading.opposite())) {
                    System.out.println("🚧 Bevægelse blokeret af væg!");
                    return false;
                }

                Player playerInFront = nextSpace.getPlayer();
                if (playerInFront != null) {
                    System.out.println(player.getName() + " forsøger at skubbe " + playerInFront.getName());

                    // *Her sikrer vi, at spilleren foran rykker sig!*
                    boolean pushed = moveForward(playerInFront);

                    if (!pushed) {
                        System.out.println("⛔ Kan ikke skubbe " + playerInFront.getName() + " videre!");
                        return false;
                    } else {
                        System.out.println("✅ " + playerInFront.getName() + " blev skubbet til "
                                + playerInFront.getSpace().x + "," + playerInFront.getSpace().y);
                    }
                }

                // *Spiller flytter sig nu, fordi pladsen er ledig*
                currentSpace.setPlayer(null);
                nextSpace.setPlayer(player);
                player.setSpace(nextSpace);

                board.incrementMoveCount();
                System.out.println(player.getName() + " rykkede til " + nextSpace.x + "," + nextSpace.y);
                return true;
            } else {
                System.out.println("❌ Ingen plads til at rykke frem!");
            }
        }
        return false;
    }
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

    // Implemented by Hannah
    public void backward(@NotNull Player player) {
        if (player == null || player.getSpace() == null) return; // Sikrer mod fejl

        Space currentSpace = player.getSpace();
        Heading oppositeHeading = player.getHeading().opposite();
        Space newSpace = board.getNeighbour(currentSpace, oppositeHeading);

        if (newSpace != null && newSpace.getPlayer() == null) {
            currentSpace.setPlayer(null);
            newSpace.setPlayer(player);
            player.setSpace(newSpace);
            board.incrementMoveCount();
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
