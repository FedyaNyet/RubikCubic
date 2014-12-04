
public class Engine {

	
	public static void main(String[] args){
		Cube cube = new Cube();
		String scrambleSequence = Cube.generateMoveSequence(6);
		cube.doMoves(scrambleSequence);
//		
//		while(cube.score()>0){
//			cube = new Cube();
//			scrambleSequence = Cube.generateMoveSequence(10);
//			cube.doMoves(scrambleSequence);
//		}
		System.out.println("Scramble: "+scrambleSequence);
		CubeSolver solver = new CubeSolver(cube, CubeSolver.ALGORITHM_DFS);
		
		long startTime = System.nanoTime();
		solver.solve();
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		String solution = solver.activeNode.breadcrumbs;
		cube.doMoves(solution);
		System.out.println("Solution: "+solution + " time:"+duration/1000000000.00+"s");
	}
}
