package agh.cs.ai2048.geometry;

import java.util.Objects;

public class Position {
  public final int x;
  public final int y;

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Position adjacent(Move move) {
    switch (move) {
      case UP: return new Position(this.x, this.y - 1);
      case RIGHT: return new Position(this.x + 1, this.y);
      case DOWN: return new Position(this.x, this.y + 1);
      case LEFT: return new Position(this.x - 1, this.y);
    }
    return null;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof Position)) return false;
    Position that = (Position) other;
    return this.x == that.x && this.y == that.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}
