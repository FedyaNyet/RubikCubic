import java.util.ArrayList;
/*		
			|#| = face index.      
	L,F,R,D,B,U = face identifier              		  	 
	 	^,>,v,< = direction of clockwise rotation.  		  	 
			 		   
	   		 v   ^		
	    	 L   R		
	         _ _ _
		  	|_|_|_|
	   		|_|5|_|
	   _ _ _|_|_|_|_ _ _ 
  U < |_|_|_|_|_|_|_|_|_| 
      |_|0|_|_|1|_|_|2|_| 
  D > |_|_|_|_|_|_|_|_|_| 
	   v   ^|_|_|_|
	   B   F|_|3|_|
		  	|=|=|=|
		  	|_|_|_|
		  	|_|4|_|
		  	|_|_|_|
 
*/
import java.util.Random;
	  
public class Cube {
	
	static final String FACE_ORDER = "LFRDBU";
	ArrayList<int[][]> faces = new ArrayList<int[][]>(FACE_ORDER.length());
	
	Cube(){
		for(char color: FACE_ORDER.toCharArray()){
			faces.add(new int[3][3]);
		}
		for(char color: FACE_ORDER.toCharArray()){
			int[][] face = new int[3][3];
			int face_index = FACE_ORDER.indexOf(color);
			for(int row=0; row<3; row++){
				for(int column=0; column<3; column++){
					face[row][column] = face_index;
				}
			}
			faces.set(face_index, face);
		}
	}
	
	private int[][] copyFace(int[][] face){
		int[][] copiedFace = face.clone();
		for (int i = 0; i < face.length; i++) {
			copiedFace[i] = face[i].clone();
		}
		return copiedFace;
	}
	
	private String stringForFace(int faceIndex){
		String ret = "";
		char color = FACE_ORDER.charAt(faceIndex);
		int[][] face = faces.get(faceIndex);
		Boolean short_line = (color == 'U' || color == 'D' || color == 'B' );
		String new_line = null;
		Object[] args =  null;
		if(short_line){
			new_line = "      |%d|%d|%d|         ";
			args = new Object[] {0, 0, 0};
			for(int row=0; row<3; row++){
				for(int column=0; column<3; column++){
					args[column] = face[row][column];
				}
				ret += String.format(new_line, args)+"\n";
			}
		}
		return ret;
	}

	private String stringForRow(int rowIndex){
		String ret = "";
		String new_line = "|%d|%d|%d|%d|%d|%d|%d|%d|%d|";
		Object[] args = new Object[] {
				faces.get(0)[rowIndex][0], faces.get(0)[rowIndex][1], faces.get(0)[rowIndex][2],
				faces.get(1)[rowIndex][0], faces.get(1)[rowIndex][1], faces.get(1)[rowIndex][2],
				faces.get(2)[rowIndex][0], faces.get(2)[rowIndex][1], faces.get(2)[rowIndex][2]
		};
		ret += String.format(new_line, args)+"\n";
		return ret;
	}

	/**
	 * Performs a single clock-wise rotation for the face at index faceIndex. This
	 * method also modifies other faces as the edge pieces of each edge are shared.
	 * 
	 * @param faceIndex
	 */
	public void rotateFace(int faceIndex){
		int[][] face = faces.get(faceIndex);
		int[][] origFace = faces.get(faceIndex).clone();
//			  | [0][0] | [0][1] | [0][2] |	   | [2][0] | [1][0] | [0][0] |
//			  | [1][0] | [1][1] | [1][2] | --> | [2][1] | [1][1] | [0][1] |
//			  | [2][0] | [2][1] | [2][2] | 	   | [2][2] | [1][2] | [0][2] |
		face[0][0] = origFace[2][0];
		face[1][0] = origFace[2][1];
		face[2][0] = origFace[2][2];
		face[0][1] = origFace[1][0];
		face[2][1] = origFace[1][2];
		face[0][2] = origFace[0][0];
		face[1][2] = origFace[0][1];
		face[2][2] = origFace[0][2];
		
		//Rotate the adjacent face's edges.
		int[][] back 	 = faces.get(FACE_ORDER.indexOf('B')); 
		int[][] up 		 = faces.get(FACE_ORDER.indexOf('U')); 
		int[][] front 	 = faces.get(FACE_ORDER.indexOf('F')); 
		int[][] down	 = faces.get(FACE_ORDER.indexOf('D')); 
		int[][] left 	 = faces.get(FACE_ORDER.indexOf('L'));
		int[][] right 	 = faces.get(FACE_ORDER.indexOf('R'));
		int[][] up_cln   = copyFace(up);
		int[][] back_cln = copyFace(back);
		switch(faceIndex){
		case 0:
			if(FACE_ORDER.indexOf('L') != faceIndex ) break;
			back[0][0] = down[0][0];
			back[1][0] 	= down[1][0];
			back[2][0] 	= down[2][0];
			down[0][0] 	= front[0][0];
			down[1][0] 	= front[1][0];
			down[2][0] 	= front[2][0];
			front[0][0]	= up[0][0];
			front[1][0]	= up[1][0];
			front[2][0]	= up[2][0];
			up[0][0] 	= back_cln[0][0];
			up[1][0] 	= back_cln[1][0];
			up[2][0] 	= back_cln[2][0];
			break;
		case 1:
			if(FACE_ORDER.indexOf('F') != faceIndex ) break;
			up[2][0] 	= left[0][2];
			up[2][1] 	= left[1][2];
			up[2][2] 	= left[2][2];
			left[0][2]  = down[0][0];
			left[1][2] 	= down[0][1];
			left[2][2] 	= down[0][2];
			down[0][0] 	= right[0][0];
			down[0][1] 	= right[1][0];
			down[0][2] 	= right[2][0];
			right[0][0] = up_cln[2][0];
			right[1][0] = up_cln[2][1];
			right[2][0] = up_cln[2][2];	
			break;
		case 2:
			if(FACE_ORDER.indexOf('R') != faceIndex ) break;
			up[0][2] 	= front[0][2];
			up[1][2] 	= front[1][2];
			up[2][2] 	= front[2][2];
			front[0][2] = down[0][2];
			front[1][2] = down[1][2];
			front[2][2] = down[2][2];
			down[0][2] 	= back[0][2];
			down[1][2] 	= back[1][2];
			down[2][2] 	= back[2][2];
			back[0][2]  = up_cln[0][2];
			back[1][2]  = up_cln[1][2];
			back[2][2]  = up_cln[2][2];	
			break;
		case 3:
			if(FACE_ORDER.indexOf('D') != faceIndex ) break;
			back[0][0] 	= right[2][2];
			back[0][1] 	= right[2][1];
			back[0][2] 	= right[2][0];
			right[2][2] = front[2][2];
			right[2][1] = front[2][1];
			right[2][0] = front[2][0];
			front[2][2] = left[2][2];
			front[2][1] = left[2][1];
			front[2][0] = left[2][0];
			left[2][2]  = back_cln[0][0];
			left[2][1]  = back_cln[0][1];
			left[2][0]  = back_cln[0][2];	
			break;
		case 4:
			if(FACE_ORDER.indexOf('B') != faceIndex ) break;
			up[0][0] 	= right[0][2];
			up[0][1] 	= right[1][2];
			up[0][2] 	= right[2][2];
			right[0][2] = down[2][2];
			right[1][2] = down[2][1];
			right[2][2] = down[2][0];
			down[2][2] = left[2][0];
			down[2][1] = left[1][0];
			down[2][0] = left[0][0];
			left[2][0] = up_cln[0][0];
			left[1][0] = up_cln[0][1];
			left[0][0] = up_cln[0][2];
			break;
		case 5:
			if(FACE_ORDER.indexOf('U') != faceIndex ) break;
			back[2][2] 	= left[0][0];
			back[2][1] 	= left[0][1];
			back[2][0] 	= left[0][2];
			left[0][0]  = front[0][0];
			left[0][1]  = front[0][1];
			left[0][2]  = front[0][2];
			front[0][0] = right[0][0];
			front[0][1] = right[0][1];
			front[0][2] = right[0][2];
			right[0][0]  = back_cln[2][2];
			right[0][1]  = back_cln[2][1];
			right[0][2]  = back_cln[2][0];
			break;
		default:
			break;
		}
	}
	
	public void doMove(String move){
		move = move.trim();
		int faceIndex = FACE_ORDER.indexOf(move.charAt(0));
		int clicks = 1;
		if(move.length()>1){
			if( move.charAt(1) == '2'){
				clicks = 2;
			}else{
				clicks = 3;
			}
		}
		for (int a = 0; a<clicks; a++){
			this.rotateFace(faceIndex);
		}
	}
	
	/**
	 * This string executes a series of moves on the cube object.
	 * 
	 * @param moves: A string of the form <FACE>[`|1|2|3] where <FACE< is one of
	 * (L,R,F,B,U,D) followed by an optional number of rotations to perform on that face.
	 * '`' is a reverse rotation that is identical to performing 3 rotations.
	 */
	public void doMoves(String moves){
	   for(String move : moves.split(",")){
		   this.doMove(move);
	   }
	}
	
	public String toString(){
		String ret = "";
		ret += "       - - -"+"\n";
		ret += stringForFace(5);
		ret += " - - - - - - - - - \n";
		for(int a=0;a<3;a++){
			ret += stringForRow(a);
		}
		ret += " - - - - - - - - - \n";
		ret += stringForFace(3);
		ret += "       - - -"+"\n";
		ret += stringForFace(4);
		ret += "       - - -"+"\n";
		return ret;
	}	
	
	public Boolean isSolved(){
		return this.score() == 0;
	}
	
	public int score(){
		int score = 0;
		for(char color: FACE_ORDER.toCharArray()){
			int face_index = FACE_ORDER.indexOf(color);
			int[][] face = faces.get(face_index);
			for(int row=0; row<3; row++){
				for(int column=0; column<3; column++){
					if( face[column][row] != face_index){
						score--;
					}
				}
			}
		}
		return score;
	}
	
	public static String generateMoveSequence(int length){
		String sequence = "";
		for(int a = 0; a<length; a++){
			Random rand = new Random();
			int rotations = rand.nextInt(3)+1;
			int faceIndex = rand.nextInt(FACE_ORDER.length());
			sequence += FACE_ORDER.charAt(faceIndex) + "" + rotations +",";
		}
		return sequence.substring(0, sequence.length()-1);
	}
	
	public static void main(String[] args){
		Cube cube = new Cube();
		String sequence = Cube.generateMoveSequence(20);
		System.out.println("preforming: "+sequence);
		cube.doMoves(sequence);
		System.out.println(cube);
		System.out.println("score: "+cube.score());
	}
}
