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

}
