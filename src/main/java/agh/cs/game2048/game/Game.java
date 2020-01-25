package agh.cs.game2048.game;

import agh.cs.game2048.game.events.*;
import agh.cs.game2048.geometry.Move;
import agh.cs.game2048.geometry.Position;
import agh.cs.game2048.utils.RandomUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Game {
  public static final int SIZE = 4;
  public static final int WINNING_TILE_VALUE = 2048;
  private List<Tile> tiles;
  private int score;
  private List<TileEventsListener> tileChangesListeners;

  public Game() {
    this.tiles = new LinkedList<>();
    this.score = 0;
    this.tileChangesListeners = new LinkedList<>();
  }

  private Game(List<Tile> tiles, int score) {
    this.tiles = new LinkedList<>();
    this.tiles.addAll(tiles);
    this.score = score;
    this.tileChangesListeners = new LinkedList<>();
  }

  public void addTileChangesListener(TileEventsListener listener) {
    this.tileChangesListeners.add(listener);
  }

  private void notifyTileChangesListeners(List<TileEvent> tileEvents) {
    this.tileChangesListeners.forEach(listener -> listener.onTileChanges(tileEvents));
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

  public void initializeRandomTiles(int numberOfTiles) {
    final var tileEvents = IntStream.range(0, numberOfTiles)
        .mapToObj(i -> this.addRandomTile())
        .flatMap(Optional::stream)
        .map(tile -> (TileEvent) new TileAddedEvent(tile))
        .collect(Collectors.toList());
    this.notifyTileChangesListeners(tileEvents);
  }

  public void step(Move move) {
    final var tileEvents = this.move(move);
    if (!tileEvents.isEmpty()) {
      this.addRandomTile().ifPresent(tile -> tileEvents.add(new TileAddedEvent(tile)));
      this.notifyTileChangesListeners(tileEvents);
    }
  }

  public boolean anyMovePossibility() {
    if (this.tiles.size() != SIZE * SIZE) {
      return true;
    }

    return Stream.of(Move.UP, Move.RIGHT).anyMatch(move ->
        this.tiles.stream().anyMatch(tile -> {
          Position adjacentPosition = tile.getPosition().adjacent(move);
          return this.isOnBoard(adjacentPosition)
              && this.tileAt(adjacentPosition)
              .map(adjacentTile -> adjacentTile.getValue() == tile.getValue())
              .orElseThrow(); /* We check for empty positions at the very beginning. */
        }));
  }

  public List<Move> possibleMoves() {
    return Stream.of(Move.UP, Move.RIGHT, Move.DOWN, Move.LEFT)
        .filter(move ->
            this.tiles.stream().anyMatch(tile -> {
              Position adjacentPosition = tile.getPosition().adjacent(move);
              return this.isOnBoard(adjacentPosition)
                  && this.tileAt(adjacentPosition)
                  .map(adjacentTile -> adjacentTile.getValue() == tile.getValue())
                  .orElse(true); /* Empty position. */
            })
        )
        .collect(Collectors.toList());
  }

  public int getScore() {
    return this.score;
  }

  public List<Tile> getTiles() {
    return this.tiles;
  }

  public Game fork() {
    final var tileClones = this.tiles.stream().map(Tile::clone).collect(Collectors.toList());
    return new Game(tileClones, this.score);
  }

  public boolean hasWinningTile() {
    return this.tiles.stream().anyMatch(tile -> tile.getValue() == WINNING_TILE_VALUE);
  }

  public void reset() {
    this.score = 0;
    final var tileEvents = this.tiles.stream()
        .map(tile -> (TileEvent) new TileVanishedEvent(tile))
        .collect(Collectors.toList());
    this.tiles.clear();
    this.notifyTileChangesListeners(tileEvents);
  }

  private Optional<Tile> addRandomTile() {
    final var free = this.freePositions().collect(Collectors.toList());
    if (free.isEmpty()) return Optional.empty();
    final var position = RandomUtils.randomElement(free);
    final var tile = new Tile(2, position);
    this.tiles.add(tile);
    return Optional.of(tile);
  }

  private List<TileEvent> move(Move move) {
    List<TileEvent> tileEvents = new LinkedList<>();
    this.linesForMove(move).forEach(positionStack -> {
      final var tileStack = positionStack.stream()
          .map(this::tileAt)
          .flatMap(Optional::stream)
          .collect(Collectors.toCollection(Stack<Tile>::new));
      while (!tileStack.isEmpty()) {
        final var tile = tileStack.pop();
        final var destination = positionStack.pop();
        if (!tileStack.isEmpty()) {
          final var prevTile = tileStack.peek();
          if (prevTile.getValue() == tile.getValue()) {
            tileStack.pop();
            tile.setPosition(destination);
            prevTile.setPosition(destination);
            tile.doubleValue();
            this.score += tile.getValue();
            this.tiles.remove(prevTile);
            tileEvents.add(new TileVanishedEvent(prevTile));
            tileEvents.add(new TileChangedEvent(tile));
          }
        }
        if (!tile.getPosition().equals(destination)) {
          tile.setPosition(destination);
          tileEvents.add(new TileChangedEvent(tile));
        }
      }
    });
    return tileEvents;
  }

  private Optional<Tile> tileAt(Position position) {
    return this.tiles.stream()
        .filter(tile -> tile.getPosition().equals(position))
        .findFirst();
  }

  private Collection<Stack<Position>> linesForMove(Move move) {
    final var lines = this.allPositions()
        .collect(Collectors.groupingBy(
            position -> move.isHorizontal() ? position.y : position.x,
            Collectors.toCollection(Stack<Position>::new)
        ))
        .values();
    final var isBackwards = move == Move.UP || move == Move.LEFT;
    if (isBackwards) {
      lines.forEach(Collections::reverse);
    }
    return lines;
  }

  private boolean isOnBoard(Position position) {
    return 0 <= position.x && position.x < SIZE && 0 <= position.y && position.y < SIZE;
  }
}
