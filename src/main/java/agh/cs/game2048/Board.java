package agh.cs.game2048;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Board extends Pane {
  public static final int N = 4;
  public static final int CELL_SIZE = 100;
  public static final int BORDER_SIZE = 16;

  private Game game = new Game();

  public Board() {
    final var boardSize = this.getSize();
    this.setPrefSize(boardSize, boardSize);
    this.setMaxSize(boardSize, boardSize);
    this.getStyleClass().add("board");
    // Add background cells.
    this.getPositions().forEach(position -> {
      final var tile = new Region();
      tile.setPrefSize(CELL_SIZE, CELL_SIZE);
      tile.setLayoutX(position.x * CELL_SIZE + (position.x + 1) * BORDER_SIZE);
      tile.setLayoutY(position.y * CELL_SIZE + (position.y + 1) * BORDER_SIZE);
      tile.getStyleClass().addAll("tile", "empty");
      this.getChildren().add(tile);
    });
    // Tmp
    for (int i = 0; i < 3; i++) {
      this.game.addRandomTile().ifPresent(t -> {
        final var position = t.getPosition();
        final var tile = new Label("2");
        tile.setPrefSize(CELL_SIZE, CELL_SIZE);
        tile.setAlignment(Pos.CENTER);
        tile.setLayoutX(position.x * CELL_SIZE + (position.x + 1) * BORDER_SIZE);
        tile.setLayoutY(position.y * CELL_SIZE + (position.y + 1) * BORDER_SIZE);
        tile.getStyleClass().add("tile");
        this.getChildren().add(tile);
      });
    }
  }

  public int getSize() {
    return (N + 1) * BORDER_SIZE + N * CELL_SIZE;
  }

  public Stream<Position> getPositions() {
    return IntStream.range(0, N).boxed()
        .flatMap(x -> IntStream.range(0, N).boxed().map(y -> new Position(x, y)));
  }
}
