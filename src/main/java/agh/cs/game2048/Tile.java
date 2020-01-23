package agh.cs.game2048;

public class Tile {
  private int value;
  private Position position;

  public Tile(int value, Position position) {
    this.value = value;
    this.position = position;
  }

  public Position getPosition() {
    return this.position;
  }

  public int getValue() {
    return this.value;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public void doubleValue() {
    this.value *= 2;
  }
}
