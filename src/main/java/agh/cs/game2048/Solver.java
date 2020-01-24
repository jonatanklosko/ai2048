package agh.cs.game2048;

import agh.cs.game2048.geometry.Move;
import agh.cs.game2048.utils.RandomUtils;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solver {
  public static final int NUMBER_OF_RUNS = 50;

  static class RunResult {
    public final int score;
    public final Move firstMove;

    public RunResult(int score, Move firstMove) {
      this.score = score;
      this.firstMove = firstMove;
    }
  }

  private Game game;

  public Solver(Game game) {
    this.game = game;
  }

  public Move bestMove() {
    return IntStream.range(0, NUMBER_OF_RUNS)
        .parallel()
        .mapToObj(i -> this.randomRun())
        .collect(Collectors.groupingBy(result -> result.firstMove))
        .entrySet()
        .stream()
        .max(Comparator.comparing(entry ->
            entry.getValue().stream().collect(Collectors.averagingDouble(result -> result.score))
          )
        )
        .map(Map.Entry::getKey)
        .orElseThrow();
  }

  private RunResult randomRun() {
    Game game = this.game.fork();
    Move firstMove = RandomUtils.randomElement(game.possibleMoves());
    game.step(firstMove);
    while (game.anyMovePossibility()) {
      game.step(Move.randomMove());
    }
    return new RunResult(game.getScore(), firstMove);
  }
}
