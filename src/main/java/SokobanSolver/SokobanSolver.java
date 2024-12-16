package SokobanSolver;

import java.io.IOException;
import com.example.boxgame.BoxGame;

import static com.example.boxgame.BoxGame.calFinish;
import static com.example.boxgame.BoxGame.solverPath;
/**
 * Command line interface for solving Sokoban with:
 * - BFS
 * - DFS
 * - Uniform cost search
 * - Greedy best first search
 * - A* search
 */

public class SokobanSolver {

	public static void parseArguments(String[] args) {
		if(args[1].equals("-q")){
			try {
				// TODO some form of input validation
				String flag = args[0];
				String puzzlePath = args[1];
				BoardState initialBoard = BoardState.parseBoardInput();
				AbstractSolver solver = null;
				System.out.println(initialBoard);
				if (flag.equals("-b")) {
					solver = new BFSSolver(initialBoard);
				}
				else if (flag.equals("-d")) {
					solver = new DFSSolver(initialBoard);
				}
				else if (flag.equals("-u")) {
					solver = new UniformCostSolver(initialBoard);
				}
				else if (flag.equals("-ab")) {
					solver = new AStarSolver(initialBoard, new BoxGoalHeuristic());
				}
				else if (flag.equals("-gb")) {
					solver = new GreedyBFSSolver(initialBoard, new BoxGoalHeuristic());
				}
				else if (flag.equals("-am")) {
					solver = new AStarSolver(initialBoard, new ManhattanHeuristic());
				}
				else if (flag.equals("-gm")) {
					solver = new GreedyBFSSolver(initialBoard, new ManhattanHeuristic());
				}
				else {
					System.out.println("Invalid command");
				}

				if (solver != null) {
					String solution = solver.search();
					solverPath = solution;
					calFinish = true;
					int nodesExplored = solver.getNodesExplored();
					int previouslySeen = solver.getPreviouslySeen();
					int queueLength = solver.getFringeLength();
					int visitedLength = solver.getVisitedLength();
					long timeElapsed = solver.getElapsedTimeMillis();
					System.out.println("Solution: " + solution);
					System.out.println("Nodes explored: " + nodesExplored);
					System.out.println("Previously seen: " + previouslySeen);
					System.out.println("Fringe: " + queueLength);
					System.out.println("Explored set: " + visitedLength);
					System.out.println("Millis elapsed: " + timeElapsed);
				}
			}catch (NoSolutionException e) {
				System.out.println("Solution does not exist");
			}
		}else {
			try {
				// TODO some form of input validation
				String flag = args[0];
				String puzzlePath = args[1];
				BoardState initialBoard = BoardState.parseBoardInput(puzzlePath);
				AbstractSolver solver = null;
				System.out.println(initialBoard);
				if (flag.equals("-b")) {
					solver = new BFSSolver(initialBoard);
				}
				else if (flag.equals("-d")) {
					solver = new DFSSolver(initialBoard);
				}
				else if (flag.equals("-u")) {
					solver = new UniformCostSolver(initialBoard);
				}
				else if (flag.equals("-ab")) {
					solver = new AStarSolver(initialBoard, new BoxGoalHeuristic());
				}
				else if (flag.equals("-gb")) {
					solver = new GreedyBFSSolver(initialBoard, new BoxGoalHeuristic());
				}
				else if (flag.equals("-am")) {
					solver = new AStarSolver(initialBoard, new ManhattanHeuristic());
				}
				else if (flag.equals("-gm")) {
					solver = new GreedyBFSSolver(initialBoard, new ManhattanHeuristic());
				}
				else {
					System.out.println("Invalid command");
				}

				if (solver != null) {
					String solution = solver.search();
					int nodesExplored = solver.getNodesExplored();
					int previouslySeen = solver.getPreviouslySeen();
					int queueLength = solver.getFringeLength();
					int visitedLength = solver.getVisitedLength();
					long timeElapsed = solver.getElapsedTimeMillis();
					System.out.println("Solution: " + solution);
					System.out.println("Nodes explored: " + nodesExplored);
					System.out.println("Previously seen: " + previouslySeen);
					System.out.println("Fringe: " + queueLength);
					System.out.println("Explored set: " + visitedLength);
					System.out.println("Millis elapsed: " + timeElapsed);
				}
			} catch (IOException e) {
				System.out.println("Puzzle file not found");
			} catch (NoSolutionException e) {
				System.out.println("Solution does not exist");
			}
		}

	}

}