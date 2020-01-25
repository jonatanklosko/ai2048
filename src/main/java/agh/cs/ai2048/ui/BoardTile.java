package agh.cs.ai2048.ui;

import agh.cs.ai2048.geometry.Position;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.util.Duration;

import static agh.cs.ai2048.ui.Board.BORDER_SIZE;
import static agh.cs.ai2048.ui.Board.TILE_SIZE;

public class BoardTile extends Label {
  private static Duration MOVE_DURATION = Duration.millis(60);
  private static Duration APPEARANCE_DURATION = Duration.millis(100);

  public BoardTile(Position position, int value) {
    this.setPrefSize(TILE_SIZE, TILE_SIZE);
    this.setAlignment(Pos.CENTER);
    this.setLayoutX(this.calculateX(position));
    this.setLayoutY(this.calculateY(position));
    this.getStyleClass().add("tile");
    this.updateValue(value);
  }

  public Timeline animateMoveTo(Position position) {
    final var timeline = new Timeline();
    final var kvX = new KeyValue(this.layoutXProperty(), this.calculateX(position));
    final var kvY = new KeyValue(this.layoutYProperty(), this.calculateY(position));
    final var kfX = new KeyFrame(MOVE_DURATION, kvX);
    final var kfY = new KeyFrame(MOVE_DURATION, kvY);
    timeline.getKeyFrames().add(kfX);
    timeline.getKeyFrames().add(kfY);
    return timeline;
  }

  public ScaleTransition animateAppearance() {
    this.setScaleX(0);
    this.setScaleY(0);
    final var scaleTransition = new ScaleTransition(APPEARANCE_DURATION, this);
    scaleTransition.setToX(1.0);
    scaleTransition.setToY(1.0);
    scaleTransition.setInterpolator(Interpolator.EASE_OUT);
    return scaleTransition;
  }

  public ScaleTransition animateDisappearance() {
    final var scaleTransition = new ScaleTransition(APPEARANCE_DURATION, this);
    scaleTransition.setToX(0);
    scaleTransition.setToY(0);
    scaleTransition.setInterpolator(Interpolator.EASE_OUT);
    return scaleTransition;
  }

  public void updateValue(int value) {
    this.setText(String.valueOf(value));
    this.getStyleClass().add("tile-" + value);
  }

  private int calculateX(Position position) {
    return position.x * TILE_SIZE + (position.x + 1) * BORDER_SIZE;
  }

  private int calculateY(Position position) {
    return position.y * TILE_SIZE + (position.y + 1) * BORDER_SIZE;
  }
}
