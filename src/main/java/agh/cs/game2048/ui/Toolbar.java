package agh.cs.game2048.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class Toolbar extends HBox {
  public Toolbar(Controller controller) {
    final var newGame = new Button("New game");
    newGame.getStyleClass().add("button");
    newGame.setOnAction(event -> controller.newGame());

    final var run = new Button("Run");
    run.getStyleClass().add("button");
    run.setOnAction(event -> controller.toggleRunning());
    run.disableProperty().bind(controller.gameOver);

    this.getChildren().addAll(newGame, run);
    this.getStyleClass().add("toolbar");
  }
}
