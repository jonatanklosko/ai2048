package agh.cs.ai2048.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
  private static final String TITLE = "AI 2048";

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    final var controller = new Controller();

    final var appContainer = new VBox();
    appContainer.getStyleClass().add("app-container");

    final var navbar = new Navbar(controller);
    final var board = new Board(controller);
    final var toolbar = new Toolbar(controller);
    appContainer.getChildren().addAll(navbar, board, toolbar);

    final var scene = new Scene(appContainer);
    scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

    controller.registerKeybindings(scene);

    stage.setScene(scene);
    stage.setTitle(TITLE);
    stage.setResizable(false);
    stage.show();
  }
}
