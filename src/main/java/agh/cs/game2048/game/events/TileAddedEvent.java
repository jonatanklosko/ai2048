package agh.cs.game2048.game.events;

import agh.cs.game2048.game.Tile;

public class TileAddedEvent extends TileEvent {
  public TileAddedEvent(Tile tile) {
    super(tile);
  }
}
