package agh.cs.game2048.game.events;

import agh.cs.game2048.game.Tile;

public class TileVanishedEvent extends TileEvent {
  public TileVanishedEvent(Tile tile) {
    super(tile);
  }
}
