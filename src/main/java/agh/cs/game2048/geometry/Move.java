package agh.cs.game2048.geometry;

import agh.cs.game2048.utils.RandomUtils;

public enum Move {
  UP,
  RIGHT,
  DOWN,
  LEFT;

  public static Move randomMove() {
    return RandomUtils.randomElement(Move.values());
  }

  public boolean isVertical() {
    return this == UP || this == DOWN;
  }

  public boolean isHorizontal() {
    return this == RIGHT || this == LEFT;
  }
}
