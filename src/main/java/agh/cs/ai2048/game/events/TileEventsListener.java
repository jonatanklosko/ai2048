package agh.cs.ai2048.game.events;

import java.util.List;

public interface TileEventsListener {
  void onTileChanges(List<TileEvent> tileEvents);
}
