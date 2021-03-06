package agh.cs.ai2048.ui;

import agh.cs.ai2048.game.Game;
import agh.cs.ai2048.game.Tile;
import agh.cs.ai2048.game.events.TileAddedEvent;
import agh.cs.ai2048.game.events.TileEvent;
import agh.cs.ai2048.game.events.TileVanishedEvent;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board extends Pane {
  public static final int TILE_SIZE = 100;
  public static final int BORDER_SIZE = 16;

  private Map<Tile, BoardTile> boardTiles;
  private BoardOverlay overlay;

  public Board(Controller controller) {
    this.boardTiles = new HashMap<>();
    this.overlay = null;

    final var game = controller.getGame();
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

    game.getTiles().forEach(tile -> this.addTileAndAnimate(tile).play());
    game.addTileChangesListener(this::onTileChanges);

    controller.gameOver.addListener((observableValue, oldValue, newValue) -> {
      if (newValue) {
        this.showGameOverOverlay();
      } else {
        this.removeOverlay();
      }
    });
    controller.gameWon.addListener((observableValue, oldValue, newValue) -> {
      if (newValue) {
        this.showGameWonOverlay();
      } else {
        this.removeOverlay();
      }
    });
  }

  public int getSize() {
    return (Game.SIZE + 1) * BORDER_SIZE + Game.SIZE * TILE_SIZE;
  }

  public void onTileChanges(List<TileEvent> tileEvents) {
    final var animations = tileEvents.stream().map(tileEvent -> {
      Tile tile = tileEvent.getTile();
      if (tileEvent instanceof TileAddedEvent) {
        return this.addTileAndAnimate(tile);
      } else if (tileEvent instanceof TileVanishedEvent) {
        return this.removeTileAndAnimate(tile);
      } else {
        return this.updateTileAndAnimate(tile);
      }
    }).toArray(Animation[]::new);
    new ParallelTransition(animations).play();
  }

  private Animation addTileAndAnimate(Tile tile) {
    final var boardTile = new BoardTile(tile.getPosition(), tile.getValue());
    this.getChildren().add(boardTile);
    this.boardTiles.put(tile, boardTile);
    return boardTile.animateAppearance();
  }

  private Animation updateTileAndAnimate(Tile tile) {
    final var boardTile = this.boardTiles.get(tile);
    final var timeline = boardTile.animateMoveTo(tile.getPosition());
    timeline.setOnFinished(event -> boardTile.updateValue(tile.getValue()));
    return timeline;
  }

  private Animation removeTileAndAnimate(Tile tile) {
    final var boardTile = this.boardTiles.get(tile);
    final var disappearance = boardTile.animateDisappearance();
    disappearance.setOnFinished(event -> {
      this.getChildren().remove(boardTile);
      this.boardTiles.remove(tile);
    });
    return new ParallelTransition(this.updateTileAndAnimate(tile), disappearance);
  }

  private void showGameOverOverlay() {
    this.overlay = new BoardOverlay("Game over!", this, false);
    this.getChildren().add(this.overlay);
    this.overlay.animateAppearance().play();
  }

  private void showGameWonOverlay() {
    this.overlay = new BoardOverlay("You win!", this, true);
    this.getChildren().add(this.overlay);
    final var keepGoing = new Button("Keep going");
    keepGoing.getStyleClass().add("button");
    keepGoing.setOnAction(event -> this.removeOverlay());
    this.overlay.getChildren().add(keepGoing);
    this.overlay.animateAppearance().play();
  }

  private void removeOverlay() {
    final var animation = this.overlay.animateDisappearance();
    animation.setOnFinished(event -> {
      this.getChildren().remove(this.overlay);
      this.overlay = null;
    });
    animation.play();
  }
}
