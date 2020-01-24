package agh.cs.game2048.ui;

import agh.cs.game2048.Game;
import agh.cs.game2048.Solver;
import agh.cs.game2048.geometry.Move;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Optional;

public class Controller {
  private Game game;
  private Solver solver;

  public Controller() {
    this.game = new Game();
    this.solver = new Solver(game);
  }

  public Game getGame() {
    return this.game;
  }

  public void registerKeybindings(Scene scene) {
    scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
      this.keyCodeToMove(event.getCode()).ifPresent(move -> this.game.step(move));
    });
  }

  private Optional<Move> keyCodeToMove(KeyCode keyCode) {
    switch (keyCode) {
      case UP: return Optional.of(Move.UP);
      case RIGHT: return Optional.of(Move.RIGHT);
      case DOWN: return Optional.of(Move.DOWN);
      case LEFT: return Optional.of(Move.LEFT);
      default: return Optional.empty();
    }
  }

  public void onRun() {
    this.game.addTileChangesListener(tileEvents -> {
      new Thread(() -> {
        long startTime = System.nanoTime();
        Move move = solver.bestMove();
        long stopTime = System.nanoTime();
        System.out.println((stopTime - startTime) / 1000000);
        Platform.runLater(() -> {
          game.step(move);
        });
      }).start();
    });
  }
}
