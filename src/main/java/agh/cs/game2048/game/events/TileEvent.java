package agh.cs.game2048.game.events;

import agh.cs.game2048.game.Tile;

public class TileEvent {
  public final Tile tile;

  public TileEvent(Tile tile) {
    this.tile = tile;
  }
}
