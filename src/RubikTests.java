import static org.junit.Assert.*;
import org.junit.Test;


public class RubikTests {

	@Test
	public void reverseIdentity() {
	   Cube cube = new Cube();
	   cube.doMoves("U,U3,D,D3,L,L3,R,R3,F,F3,B,B3");
	   assertTrue("Reverse:", cube.isSolved());
	 } 
	
	@Test
	public void tripletPlusOne(){
	   Cube cube = new Cube();
	   cube.doMoves("F3,F,B3,B,U3,U,D3,D,L3,L,R3,R");
	   assertTrue("Triples plus single:",cube.isSolved());
	}
	
	@Test
	public void sequenceTest(){
		String sequence = Cube.generateMoveSequence(5);
		assertTrue("There are 5 move:",sequence.split(",").length == 5);
	}
	
	@Test
	public void nonTrivialMoveSolutionTest(){
		Cube cube = new Cube();
		cube.doMoves("R,D2,F");
		assertTrue("Cube is scrambled:", !cube.isSolved());
		cube.doMoves("F3,D2,R3");
		assertTrue("Simple non sequencial problem solved:", cube.isSolved());
	}
	
	@Test
	public void testCrossSolution(){
		Cube cube = new Cube();
		cube.doMoves("F,L,B,R,F,L,B,R,F,L,D2,L,U2,L3");
		assertTrue("Cube Has Cross:", cube.faceHasCross(Cube.FACE_ORDER.indexOf('D')));
	}
	
	@Test
	public void testFaceSolution(){
		Cube cube = new Cube();
		cube.doMoves("D2,U,B3,D3,L2,L2,B,U3,L3,D2,L");
		assertTrue("Cube Has Cross:", cube.faceIsComplete(Cube.FACE_ORDER.indexOf('U')));
		cube = new Cube();
		cube.doMoves("L2,B2,R3,B3,F3,D2,F3,R2,F2");
		assertTrue("Cube Has Cross:", cube.faceIsComplete(Cube.FACE_ORDER.indexOf('L')));
	}
	
	@Test
	public void testTwoRowSolution(){
		Cube cube = new Cube();
		cube.doMoves("D");
		assertTrue("Cube Has Two Rows:", cube.faceHasSecondRow(Cube.FACE_ORDER.indexOf('U')));
	}
	
	@Test
	public void rotatedInPlaceCorner(){
		Cube cube = new Cube();
		cube.doMoves("R2,U3,B2,F2,D2,B2,U3,F2,D2,F,R2,F,D2,F2,D2");
		System.out.println(cube);
		int placedShips = cube.hasRotatedInplaceCorner(0);
		assertTrue("Cube Has Two Rows:", placedShips==1);
	}
}
