package agh.cs.game2048.ui;

import agh.cs.game2048.game.Game;
import agh.cs.game2048.game.Solver;
import agh.cs.game2048.geometry.Move;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class Controller {
  public static final int MIN_SOLVER_STEP_MS = 60;

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
    if (this.solverRunning.get()) {
      this.solverRunning.set(false);
    } else {
      this.solverRunning.set(true);
      final var thread = this.createSolverThread();
      thread.setDaemon(true);
      thread.start();
    }
  }

  public void newGame() {
    this.game.reset();
    this.game.initializeRandomTiles(2);
  }

  private Thread createSolverThread() {
    return new Thread(() -> {
      final var waitForGameUpdate = new AtomicBoolean(false);
      while (this.solverRunning.get()) {
        final var searchStartTime = System.nanoTime();
        final var move = solver.bestMove();
        final var searchEndTime = System.nanoTime();
        final var millisLeft = 60 - (searchEndTime - searchStartTime) / 1000000;
        if (millisLeft > 0) {
          try {
            Thread.sleep(millisLeft);
          } catch (InterruptedException exception) {
            return;
          }
        }
        /* The actual update must be run in the UI thread, so we need to wait for it to figure out the next move. */
        waitForGameUpdate.set(true);
        Platform.runLater(() -> {
          game.step(move);
          if (!this.game.anyMovePossibility()) {
            this.solverRunning.set(false);
          }
          waitForGameUpdate.set(false);
        });
        while (waitForGameUpdate.get()) {}
      }
    });
  }
}
