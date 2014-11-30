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
	public void GenerateSequenceTest(){
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
	public void scrambleTest(){
		String solution = 	"       - - -\n"+
							"      |G|G|G|         \n"+
							"      |W|W|O|         \n"+
							"      |R|R|Y|         \n"+
							" - - - - - - - - - \n"+
							"|O|B|B|W|W|B|R|G|R|\n"+
							"|G|R|Y|R|B|O|B|O|O|\n"+
							"|G|R|O|G|Y|O|B|Y|Y|\n"+
							" - - - - - - - - - \n"+
							"      |Y|B|W|         \n"+
							"      |B|Y|O|         \n"+
							"      |R|R|O|         \n"+
							"       - - -\n"+
							"      |Y|G|B|         \n"+
							"      |Y|G|W|         \n"+
							"      |W|W|W|         \n"+
							"       - - -\n";
		String sequence;
		sequence = "D,B,R,L,U,F";
		Cube cube = new Cube();
		cube.doMoves(sequence);
		assertTrue("Soluion matches: ",cube.toRBGString().equals(solution));
	}
	
}
