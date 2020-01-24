package agh.cs.game2048.game.events;

import agh.cs.game2048.game.Tile;

public class AbstractTileEvent implements TileEvent {
  private final Tile tile;

  public AbstractTileEvent(Tile tile) {
    this.tile = tile;
  }

  public Tile getTile() {
    return this.tile;
  }
}
