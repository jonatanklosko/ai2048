package agh.cs.game2048;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
  private static final String TITLE = "2048";

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    final var board = new Board();
    final var vbox = new VBox();
    vbox.getChildren().add(board);
    vbox.setAlignment(Pos.CENTER);
    final var scene = new Scene(vbox, board.getSize() + 50, board.getSize() + 50);
    scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

    stage.setScene(scene);
    stage.setTitle(TITLE);
    stage.setResizable(false);
    stage.show();
  }
}
