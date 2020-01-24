package agh.cs.game2048.game.events;

import java.util.List;

public interface TileChangesListener {
  void onTileChanges(List<TileEvent> tileEvents);
}
