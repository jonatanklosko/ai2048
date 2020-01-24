package agh.cs.game2048.ui;

import agh.cs.game2048.Game;
import agh.cs.game2048.Solver;
import agh.cs.game2048.geometry.Move;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class Controller {
  private Game game;
  private Solver solver;
  private AtomicBoolean solverRunning;

  public Controller() {
    this.game = new Game();
    this.solver = new Solver(game);
    this.solverRunning = new AtomicBoolean(false);

    this.game.initializeRandomTiles(2);
  }

  public Game getGame() {
    return this.game;
  }

  public void registerKeybindings(Scene scene) {
    scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
      if (!this.solverRunning.get()) {
        this.keyCodeToMove(event.getCode()).ifPresent(move -> this.game.step(move));
      }
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

  public void toggleRunning() {
    if (!this.solverRunning.get()) {
      this.solverRunning.set(true);
      final var thread = new Thread(() -> {
        final var wait = new AtomicBoolean(false);
        while (this.solverRunning.get()) {
          while (wait.get()) {}
//          Move move = solver.bestMove();
        long startTime = System.nanoTime();
        Move move = solver.bestMove();
//        long stopTime = System.nanoTime();
//        long diff = 60 - (stopTime - startTime) / 1000000;
//        try {
//          if (diff > 0) {
//          Thread.sleep(diff);
//          }
//        } catch(Exception e) {}
        System.out.println((System.nanoTime() - startTime) / 1000000);
          wait.set(true);
          Platform.runLater(() -> {
            game.step(move);
            wait.set(false);
            if (!this.game.anyMovePossibility()) {
              this.solverRunning.set(false);
            }
          });
        }
      });
      thread.setDaemon(true);
      thread.start();
    } else {
      this.solverRunning.set(false);
    }
  }

  public void newGame() {
    this.game.reset();
    this.game.initializeRandomTiles(2);
  }
}
