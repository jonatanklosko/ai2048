package agh.cs.game2048;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Board extends Pane {
  public static final int N = 4;
  public static final int CELL_SIZE = 100;
  public static final int BORDER_SIZE = 16;

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
//    for (int y = 0; y < N / 2; y++) {
//      for (int x = 0; x < N / 2; x++) {
//        final var tile = new Label("2");
//        tile.setPrefSize(100, 100);
//        tile.setAlignment(Pos.CENTER);
//        tile.setLayoutX(x * CELL_SIZE + (x + 1) * BORDER_SIZE);
//        tile.setLayoutY(y * CELL_SIZE + (y + 1) * BORDER_SIZE);
//        tile.getStyleClass().add("tile");
//        this.getChildren().add(tile);
//      }
//    }
  }

  public int getSize() {
    return (N + 1) * BORDER_SIZE + N * CELL_SIZE;
  }

  public Stream<Position> getPositions() {
    return IntStream.range(0, N).boxed()
        .flatMap(x -> IntStream.range(0, N).boxed().map(y -> new Position(x, y)));
  }
}
