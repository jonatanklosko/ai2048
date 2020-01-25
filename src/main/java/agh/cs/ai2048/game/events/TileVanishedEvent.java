package agh.cs.ai2048.game.events;

import agh.cs.ai2048.game.Tile;

public class TileVanishedEvent extends AbstractTileEvent {
  public TileVanishedEvent(Tile tile) {
    super(tile);
  }
}
