package agh.cs.game2048.ui;

import agh.cs.game2048.*;
import agh.cs.game2048.geometry.Move;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.*;

public class Board extends Pane {
  public static final int TILE_SIZE = 100;
  public static final int BORDER_SIZE = 16;

  private Map<Tile, BoardTile> boardTiles;

  public Board(Game game) {
    game.addTileChangesListener(this::onTileChanges);
    this.boardTiles = new HashMap<>();

    final var boardSize = this.getSize();
    this.setPrefSize(boardSize, boardSize);
    this.setMaxSize(boardSize, boardSize);
    this.getStyleClass().add("board");
    // Add background cells.
    game.allPositions().forEach(position -> {
      final var tile = new Region();
      tile.setPrefSize(TILE_SIZE, TILE_SIZE);
      tile.setLayoutX(position.x * TILE_SIZE + (position.x + 1) * BORDER_SIZE);
      tile.setLayoutY(position.y * TILE_SIZE + (position.y + 1) * BORDER_SIZE);
      tile.getStyleClass().addAll("tile", "empty");
      this.getChildren().add(tile);
    });

    game.initializeRandomTiles(2);

//    Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.millis(100), e -> {
//      game.step(Solver.bestMove(game));
//    }));
//    fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
//    fiveSecondsWonder.play();
//    }, 0, 60);

//    final var solver = new Solver(game);

//    new Timer().schedule(new TimerTask() {
//      @Override
//      public void run() {
////        synchronized (game) {
//          Move move = solver.bestMove();
//          Platform.runLater(() -> {
//            game.step(move);
//          });
////        }
//      }
//    }, 0, 60);


//    game.addTileChangesListener(tileEvents -> {
//      new Thread(() -> {
//        long startTime = System.nanoTime();
//        Move move = solver.bestMove();
//        long stopTime = System.nanoTime();
//        System.out.println((stopTime - startTime) / 1000000);
//        Platform.runLater(() -> {
//          game.step(move);
//        });
//      }).start();
//    });
  }

  public int getSize() {
    return (Game.SIZE + 1) * BORDER_SIZE + Game.SIZE * TILE_SIZE;
  }

  public void onTileChanges(List<TileEvent> tileEvents) {
    final var animations = tileEvents.stream().map(tileEvent -> {
      Tile tile = tileEvent.tile;
      if (tileEvent instanceof TileAddedEvent) {
        return this.addTileAndAnimate(tile);
      } else {
        boolean remove = tileEvent instanceof TileVanishedEvent;
        return this.updateTileAndAnimate(tile, remove);
      }
    }).toArray(Animation[]::new);
    final var animation = new ParallelTransition(animations);
    animation.play();
  }

  private Animation addTileAndAnimate(Tile tile) {
    final var boardTile = new BoardTile(tile.getPosition(), tile.getValue());
    this.getChildren().add(boardTile);
    this.boardTiles.put(tile, boardTile);
    return boardTile.animateAppearance();
  }

  private Animation updateTileAndAnimate(Tile tile, boolean remove) {
    final var boardTile = this.boardTiles.get(tile);
    final var timeline = boardTile.animateMoveTo(tile.getPosition());
    timeline.setOnFinished(event -> {
      boardTile.updateValue(tile.getValue());
      if (remove) {
        this.getChildren().remove(boardTile);
        this.boardTiles.remove(tile);
      }
    });
    return timeline;
  }
}
