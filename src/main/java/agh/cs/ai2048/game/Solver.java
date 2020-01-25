package agh.cs.ai2048.game;

import agh.cs.ai2048.geometry.Move;
import agh.cs.ai2048.utils.RandomUtils;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/* Solver implementing the Monte Carlo method.
   Inspired by https://towardsdatascience.com/2048-solving-2048-with-monte-carlo-tree-search-ai-2dbe76894bab */
public class Solver {
  public static final int NUMBER_OF_RUNS = 100;

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
