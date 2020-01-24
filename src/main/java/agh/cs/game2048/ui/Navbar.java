package agh.cs.game2048.ui;

import agh.cs.game2048.Game;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Navbar extends HBox {
  public Navbar(Game game) {
    final var title = new Label("2048");
    title.getStyleClass().add("title");

    Region hGrow = new Region();
    HBox.setHgrow(hGrow, Priority.ALWAYS);

    final var scoreTitle = new Label("SCORE");
    scoreTitle.getStyleClass().add("score-title");
    final var scoreValue = new Label("0");
    game.addTileChangesListener(tileEvents -> {
      scoreValue.setText(String.valueOf(game.getScore()));
    });
    scoreValue.getStyleClass().add("score-value");
    final var score = new VBox();
    score.getStyleClass().add("score");
    score.getChildren().addAll(scoreTitle, scoreValue);

    final var scoreBox = new VBox();
    scoreBox.setAlignment(Pos.CENTER);
    scoreBox.getChildren().addAll(score);

    this.getChildren().addAll(title, hGrow, scoreBox);

    this.getStyleClass().add("navbar");
  }
}
