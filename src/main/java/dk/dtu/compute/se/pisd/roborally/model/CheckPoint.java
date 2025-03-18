package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;



// Implemented by Liam.
// creating a Checkpoint class.
public class CheckPoint extends FieldAction {
    private int x;


    public int getX() {
        return x;
    }

    public CheckPoint(int x){
        this.x = x;

    }

    public void ExecuteAction(){
        System.out.println("Checkpoint reached at  (" + x + ", " + ")");
    }

    @Override

    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if(player.getCheckPointCounter() == x){
            System.out.println(player + "has reached checkpoint " + x);
            player.setCheckPointCounter(player.getCheckPointCounter() + 1);
            if(player.getCheckPointCounter() == 3){
                System.out.println(player + " has won the game");

                return true;
            }
        }
        return false;
    }
}
