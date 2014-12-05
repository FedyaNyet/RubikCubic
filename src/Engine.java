
public class Engine {
	
	public static void main(String[] args){
		
		int sequenceLength = 5;
		int algorith;
//		algorith = CubeSolver.ALGORITHM_BFS;
//		algorith = CubeSolver.ALGORITHM_DFS;
//		algorith = CubeSolver.ALGORITHM_HUMAN;
		algorith = CubeSolver.ALGORITHM_GENETIC;
		
		Cube cube = new Cube();
		String scrambleSequence = "";
	
		while(cube.score()>0){
			cube = new Cube();
			scrambleSequence = Cube.generateMoveSequence(sequenceLength);
			cube.doMoves(scrambleSequence);
		}
		
		System.out.println("Scramble: "+scrambleSequence);
		CubeSolver solver = new CubeSolver(cube, algorith);
		
		long startTime = System.nanoTime();
		solver.solve();
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		String solution = solver.activeNode.breadcrumbs;
		cube.doMoves(solution);
		System.out.println("Solution: "+solution + " time:"+duration/1000000000.00+"s");
	}
}
