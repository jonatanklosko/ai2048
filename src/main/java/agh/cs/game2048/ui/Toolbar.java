package agh.cs.game2048.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class Toolbar extends HBox {
  public Toolbar() {
    final var run = new Button("Run");
    run.getStyleClass().add("button");
    this.getChildren().add(run);

    this.getStyleClass().add("toolbar");
  }
}
