
public class Engine {

	
	public static void main(String[] args){
		Cube cube = new Cube();
		String sequence = Cube.generateMoveSequence(5);
		System.out.println("Scramble: "+sequence);
		cube.doMoves(sequence);
		CubeSolver solver = new CubeSolver(cube, CubeSolver.ALGORITHM_HUMAN);
		long startTime = System.nanoTime();
		solver.solve();
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		String solution = solver.activeNode.breadcrumbs;
		cube.doMoves(solution);
		System.out.println("Solution: "+solution + " time:"+duration/1000000000.00+"s");
	}
}
