package agh.cs.game2048;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Game {
  public static final int SIZE = 4;
  public List<Tile> tiles;

  public Game() {
    this.tiles = new LinkedList<>();
  }

  public Stream<Position> allPositions() {
    return IntStream.range(0, SIZE).boxed()
        .flatMap(x -> IntStream.range(0, SIZE).boxed().map(y -> new Position(x, y)));
  }

  public Stream<Position> freePositions() {
    return this.allPositions().filter(this::isPositionFree);
  }

  private boolean isPositionFree(Position position) {
    return this.tiles.stream().noneMatch(tile -> tile.getPosition().equals(position));
  }

  public void move(Move move) {
    this.linesForMove(move).forEach(positions -> {
      final var tileStack = new Stack<Tile>();
      tileStack.addAll(
          this.tiles.stream()
              .filter(tile -> positions.contains(tile.getPosition()))
              .collect(Collectors.toList())
      );
      final var positionStack = new Stack<Position>();
      positionStack.addAll(positions);
      while (!tiles.isEmpty()) {
        final var tile = tileStack.pop();
        final var destination = positionStack.pop();
        tile.setPosition(destination);
        if (!tiles.isEmpty()) {
          final var prevTile = tileStack.peek();
          if (prevTile.getValue() == tile.getValue()) {
            tileStack.pop();
            // todo: add to changes
            tile.doubleValue();
            this.tiles.remove(prevTile);
          }
        }
      }
    });
  }

  private Collection<List<Position>> linesForMove(Move move) {
    final var lines = this.allPositions()
        .collect(Collectors.groupingBy(
            position -> move.isHorizontal() ? position.y : position.x
        ))
        .values();
    final var isBackwards = move == Move.UP || move == Move.LEFT;
    if (isBackwards) {
      lines.forEach(Collections::reverse);
    }
    return lines;
  }

  public Optional<Tile> addRandomTile() {
    final var free = this.freePositions().collect(Collectors.toList());
    System.out.println(free);
    if (free.isEmpty()) return Optional.empty();
    final var position = RandomUtils.randomElement(free);
    final var tile = new Tile(2, position);
    this.tiles.add(tile);
    return Optional.of(tile);
  }
}
