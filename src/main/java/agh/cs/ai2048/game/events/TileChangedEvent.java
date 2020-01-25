package agh.cs.ai2048.game.events;

import agh.cs.ai2048.game.Tile;

public class TileChangedEvent extends AbstractTileEvent {
  public TileChangedEvent(Tile tile) {
    super(tile);
  }
}
