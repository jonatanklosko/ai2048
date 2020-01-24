package agh.cs.game2048.game.events;

import agh.cs.game2048.game.Tile;

public class TileChangedEvent extends AbstractTileEvent {
  public TileChangedEvent(Tile tile) {
    super(tile);
  }
}
