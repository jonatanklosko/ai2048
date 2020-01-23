package agh.cs.game2048;

public enum Move {
  UP,
  RIGHT,
  DOWN,
  LEFT;

  public boolean isVertical() {
    return this == UP || this == DOWN;
  }

  public boolean isHorizontal() {
    return this == RIGHT || this == LEFT;
  }
}
