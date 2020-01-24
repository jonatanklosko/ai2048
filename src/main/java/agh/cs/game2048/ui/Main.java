package agh.cs.game2048.ui;

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
    final var vbox = new VBox();
    final var navbar = new Navbar();
    final var board = new Board();
    final var toolbar = new Toolbar();
    vbox.getChildren().addAll(navbar, board, toolbar);
    vbox.setAlignment(Pos.CENTER);
    final var scene = new Scene(vbox);
    scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

    stage.setScene(scene);
    stage.setTitle(TITLE);
    stage.setResizable(false);
    stage.show();
  }
}
