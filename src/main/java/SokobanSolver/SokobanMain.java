package SokobanSolver;

/**
 	* Assignment 1 Sokoban Solver
 	* Forrest Knight - CS480 - Artificial Intelligence
 	* Fall 2017
 	*/
public class SokobanMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		args = new String[]{ "-b","resource/Level1.txt"};
		SokobanSolver.parseArguments(args);
	}

}
