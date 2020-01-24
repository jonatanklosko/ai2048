package agh.cs.game2048.game;

import agh.cs.game2048.geometry.Position;

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

  public Tile clone() {
    return new Tile(this.value, this.position);
  }
}
