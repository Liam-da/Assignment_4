package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;


/**
 * Represents a checkpoint in the RoboRally game. A checkpoint is an action
 * that players must reach in order to progress in the game. This class implements
 * the {@link FieldAction} interface and is responsible for managing the player's
 * progress through the checkpoints and determining if a player has won the game.
 * The class tracks a specific checkpoint identified by the x-coordinate, and
 * it executes actions when a player reaches this checkpoint.
 *
 */
public class CheckPoint extends FieldAction {
    /**
     * The x-coordinate of the checkpoint.
     */
    private int x;

    /**
     * Gets the x-coordinate of the checkpoint.
     *
     * @return the x-coordinate of the checkpoint
     */
    public int getX() {
        return x;
    }
    /**
     * Constructs a new CheckPoint object with the specified x-coordinate.
     *
     * @param x the x-coordinate of the checkpoint
     */
    public CheckPoint(int x){
        this.x = x;

    }
    /**
     * Executes the action when a player reaches this checkpoint. This method
     * prints out a message indicating that the checkpoint has been reached.
     */
    public void ExecuteAction(){
        System.out.println("Checkpoint reached at  (" + x + ", " + ")");
    }
    /**
     * Performs the action of reaching the checkpoint. If the player has not
     * yet reached this checkpoint (based on the player's checkpoint counter),
     * it increments the player's checkpoint counter. If the player reaches 3
     * checkpoints, they are declared the winner of the game.
     *
     * @param gameController the game controller to manage game logic
     * @param space the space on the board where the checkpoint is located
     * @return true if the player has won the game by reaching this checkpoint,
     *         false otherwise
     */
    @Override

    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if(player.getCheckPointCounter() == x){
            System.out.println(player + "has reached checkpoint " + x);
            player.setCheckPointCounter(player.getCheckPointCounter() + 1);
            if(player.getCheckPointCounter() == 4){
                System.out.println(player + " has won the game");
                System.exit(0);

                return true;
            }
        }
        return false;
    }
}
