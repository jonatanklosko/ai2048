package agh.cs.ai2048.ui;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class BoardOverlay extends VBox {
  public final Duration APPEARANCE_DURATION = Duration.millis(800);
  public final Duration DISAPPEARANCE_DURATION = Duration.millis(400);

  public BoardOverlay(String message, Board board, boolean gold) {
    final var boardSize = board.getSize();
    this.setPrefSize(boardSize, boardSize);
    this.getStyleClass().add("overlay");
    if (gold) {
      this.getStyleClass().add("overlay-gold");
    }

    final var overlayText = new Label(message);
    overlayText.getStyleClass().add("overlay-text");
    this.getChildren().add(overlayText);
  }

  public FadeTransition animateAppearance() {
    this.setOpacity(0);
    final var fade = new FadeTransition(APPEARANCE_DURATION, this);
    fade.setToValue(1);
    fade.setInterpolator(Interpolator.EASE_OUT);
    return fade;
  }

  public FadeTransition animateDisappearance() {
    final var fade = new FadeTransition(DISAPPEARANCE_DURATION, this);
    fade.setToValue(0);
    fade.setInterpolator(Interpolator.EASE_IN);
    return fade;
  }
}
