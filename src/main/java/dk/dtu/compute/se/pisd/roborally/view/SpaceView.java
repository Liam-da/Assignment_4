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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.CheckPoint;
import dk.dtu.compute.se.pisd.roborally.model.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;


/**
 * Represents a graphical view of a game space in the RoboRally game.
 * The SpaceView is responsible for visually displaying the state of a space on the game board,
 * including any players, walls, and actions (like checkpoints) associated with the space.
 * It reacts to changes in the state of the space and updates the view accordingly.
 *
 * This class extends {@link StackPane} to provide a container that can display different graphical
 * elements (e.g., players, walls, checkpoints) in the space.
 * It implements {@link ViewObserver} to observe changes in the space and update the view accordingly.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class SpaceView extends StackPane implements ViewObserver {

    /**
     * The height of the space view.
     */
    final public static int SPACE_HEIGHT = 40;

    /**
     * The width of the space view.
     */
    final public static int SPACE_WIDTH = 40;

    public final Space space;

    /**
     * Constructs a new SpaceView for the given space.
     * The view initializes the space's graphical representation, sets its size, and
     * attaches itself as an observer to the space to track changes in its state.
     *
     * @param space The space that this view represents.
     */
    public SpaceView(@NotNull Space space) {
        this.space = space;

        // Set size properties for the space view
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        // Alternate the background color based on the coordinates of the space
        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: white;");
        } else {
            this.setStyle("-fx-background-color: black;");
        }

        updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }

    /**
     * Updates the visual representation of the player in the space.
     * If there is a player in the space, an arrow is drawn to indicate the player's position and heading.
     */
    private void updatePlayer() {
        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0 );
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90*player.getHeading().ordinal())%360);
            this.getChildren().add(arrow);
        }
    }

    /**
     * Updates the space view whenever the state of the associated space changes.
     * The method clears the current graphical elements and re-adds the updated elements
     * (such as the player, walls, and actions).
     *
     * @param subject The subject that triggered the update. In this case, it is the associated space.
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            this.getChildren().removeIf(node -> node instanceof Polygon && !(node.getOpacity() == 0.7));

            for (FieldAction action : space.getActions()) {
                if (action instanceof CheckPoint) {
                    Circle circle = new Circle();
                    circle.setRadius(SPACE_WIDTH / 2);
                    this.getChildren().add(circle);

                    Text text = new Text(Integer.toString(((CheckPoint) action).getX()));
                    text.setFill(Color.YELLOW);
                    this.getChildren().add(text);

                }
                if (action instanceof ConveyorBelt) {
                    drawConveyorBelt(((ConveyorBelt) action).getHeading());
                }
            }

            // Add walls to the space view
            for (Heading wall : space.getWalls()) {
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.RED);

                int WALL_THICKNESS = 4;

                if (wall == Heading.NORTH) {
                    rectangle.setWidth(SPACE_WIDTH);
                    rectangle.setHeight(WALL_THICKNESS);
                    rectangle.setTranslateX(0);
                    rectangle.setTranslateY(-SPACE_HEIGHT / 2 + WALL_THICKNESS / 2);
                }

                if (wall == Heading.EAST) {
                    rectangle.setWidth(WALL_THICKNESS);
                    rectangle.setHeight(SPACE_HEIGHT);
                    rectangle.setTranslateX(SPACE_WIDTH / 2 - WALL_THICKNESS / 2);
                    rectangle.setTranslateY(0);
                }
                if (wall == Heading.SOUTH) {
                    rectangle.setWidth(SPACE_WIDTH);
                    rectangle.setHeight(WALL_THICKNESS);
                    rectangle.setTranslateX(0);
                    rectangle.setTranslateY(SPACE_HEIGHT / 2 - WALL_THICKNESS / 2);
                }
                if (wall == Heading.WEST) {
                    rectangle.setWidth(WALL_THICKNESS);
                    rectangle.setHeight(SPACE_HEIGHT);
                    rectangle.setTranslateX(-SPACE_WIDTH / 2 + WALL_THICKNESS / 2);
                    rectangle.setTranslateY(0);
                }
                this.getChildren().add(rectangle);

            }

            updatePlayer();
        }
    }

    void drawConveyorBelt(Heading heading) {
        System.out.println("🎨 Drawing conveyor belt at " + space.x + "," + space.y + " Heading: " + heading);

        Polygon arrow = new Polygon(0.0, 0.0, 15.0, 25.0, 30.0, 0.0);
        arrow.setFill(Color.DODGERBLUE); // Improve visibility
        arrow.setStroke(Color.WHITE);
        arrow.setStrokeWidth(2);
        arrow.setOpacity(0.7);
        arrow.setRotate((90 * heading.ordinal()) % 360);

        Platform.runLater(() -> {
            this.getChildren().add(arrow);
            arrow.toFront();
        });
    }

    private void updatePlayer() {
        this.getChildren().removeIf(node -> node instanceof Polygon && node.getOpacity() != 0.7); // Remove previous player arrows only

        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0, 10.0, 20.0, 20.0, 0.0);
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90 * player.getHeading().ordinal()) % 360);
            this.getChildren().add(arrow);
        }
    }

}
