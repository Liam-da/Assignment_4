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
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;


/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 40; // 60; // 75;
    final public static int SPACE_WIDTH = 40;  // 60; // 75;

    public final Space space;


    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

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

    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            this.getChildren().clear();

            for (FieldAction action: space.getActions()){
                if (action instanceof CheckPoint) {
                    Circle circle = new Circle();
                    circle.setRadius(SPACE_WIDTH/2);
                    this.getChildren().add( circle );

                    Text text = new Text(Integer.toString(((CheckPoint) action).getX()));
                    text.setFill(Color.YELLOW);
                    this.getChildren().add( text );

                }
            }

            // ikke helt rigtig implementation!!!
            space.getWalls();
            for (Heading wall: space.getWalls()){
                if (wall == Heading.NORTH){
                    Rectangle rectangle = new Rectangle();
                    rectangle.setX(2);
                    rectangle.setY(2);
                    rectangle.setWidth(SPACE_WIDTH);
                    rectangle.setHeight(2);
                    rectangle.setFill(Color.RED);
                    this.getChildren().add(rectangle);

                }

                if (wall == Heading.EAST){
                    Rectangle rectangle = new Rectangle();
                    rectangle.setX(SPACE_WIDTH - 1);
                    rectangle.setY(0);
                    rectangle.setWidth(1);
                    rectangle.setHeight(SPACE_HEIGHT);
                    rectangle.setFill(Color.RED);
                    this.getChildren().add(rectangle);
                }
                if (wall == Heading.SOUTH){
                    Rectangle rectangle = new Rectangle();
                    rectangle.setX(10);
                    rectangle.setY(10);
                    rectangle.setWidth(5);
                    rectangle.setHeight(2);
                    rectangle.setFill(Color.RED);
                    this.getChildren().add(rectangle);
                }
                if (wall == Heading.WEST){
                    Rectangle rectangle = new Rectangle();
                    rectangle.setX(0);
                    rectangle.setY(0);
                    rectangle.setWidth(1);
                    rectangle.setHeight(SPACE_HEIGHT);
                    rectangle.setFill(Color.RED);
                    this.getChildren().add(rectangle);
                }
            }

            updatePlayer();
        }
    }

}
