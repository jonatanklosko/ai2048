package agh.cs.game2048.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class Toolbar extends HBox {
  public Toolbar(Controller controller) {
    final var run = new Button("Run");
    run.getStyleClass().add("button");
    run.setOnAction(event -> controller.onRun());
    this.getChildren().add(run);

    this.getStyleClass().add("toolbar");
  }
}
