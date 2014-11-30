
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import FastCopy.DeepCopy;

public class CubeSolver {

	static final int MAX_DEPTH = 10;
	static final int ALGORITHM_DFS = 0;
	static final int ALGORITHM_BFS = 1;
	static final int ALGORITHM_HUMAN = 2;
	
	int algorithm;
	int search_depth = -1;
	CubeNode activeNode;
	LinkedList<CubeNode> queue;
	
	CubeSolver(Cube cube){
		initWithCube(cube);
	}
	
	CubeSolver(Cube cube, int algorithm){
		initWithCube(cube);
		this.algorithm = algorithm;
	}
	
	private void initWithCube(Cube cube){
		this.activeNode =  new CubeNode(cube);
		queue = new LinkedList<CubeNode>();
		queue.add(this.activeNode);
		this.algorithm = ALGORITHM_BFS;
	}

	private void stepSolveBFS(){
		activeNode = queue.pop();
		if(activeNode.cube.isSolved())
			return;
		activeNode.defineChildren();
		Iterator<CubeNode> entries = activeNode.children.values().iterator();
		while (entries.hasNext()) {
			queue.add(entries.next());
		}
	}
	
	private void stepSolveDFS(){
		if(activeNode.cube.isSolved())
			return;
		if(activeNode.depth >= CubeSolver.MAX_DEPTH || (search_depth > 0 && activeNode.depth >= search_depth )){
			activeNode.parent.children.remove(activeNode.move);
			activeNode = activeNode.parent;
			return;
		}
		if(activeNode.children == null){
			activeNode.defineChildren();
		}
		Iterator<CubeNode> entries = activeNode.children.values().iterator();
		if(!entries.hasNext()){
			activeNode.parent.children.remove(activeNode.move);
			activeNode = activeNode.parent;
			return;
		}else{
			activeNode = entries.next();
			return;
		}
	}
	
	
	int humanStep = 0;
	int correctCorners = 0;
	int solvingFace = 0;
	int correctSecondLevelEdges = 0;
	private void stepHuman(){
		if(humanStep == 0){
			if(this.activeNode.cube.faceHasCross(solvingFace)){
				this.solvingFace = solvingFace;
				correctCorners = this.activeNode.cube.getNumerOfCorrectCorners(solvingFace);
				this.queue.clear();
				this.queue.add(this.activeNode);
				this.search_depth =+ activeNode.depth + 5;
				System.out.println(humanStep+") CROSS. faceColor:"+solvingFace+ " crumbs:"+this.activeNode.breadcrumbs + " depth:"+this.activeNode.depth);
				System.out.println(this.activeNode.cube);
				humanStep++;
			}
		}
		else if(humanStep == 1 && this.activeNode.cube.faceHasCross(solvingFace)){
			if(correctCorners < this.activeNode.cube.getNumerOfCorrectCorners(solvingFace)){
				this.queue.clear();
				this.queue.add(this.activeNode);
				this.search_depth =+ activeNode.depth + 5;
				int twistedNodes = this.activeNode.cube.hasRotatedInplaceCorner(solvingFace);
				if(this.activeNode.cube.hasRotatedInplaceCorner(solvingFace) > 0){
					System.out.println("twistedNodes:"+twistedNodes);
					System.out.println(this.activeNode.cube);
					this.search_depth =+ activeNode.depth + 7;
				}
				correctCorners = this.activeNode.cube.getNumerOfCorrectCorners(solvingFace);
				System.out.println(humanStep+") New Corner. faceColor:"+solvingFace+ " corners: "+correctCorners+" crumbs:"+this.activeNode.breadcrumbs + " depth:"+this.activeNode.depth);
				System.out.println(this.activeNode.cube);
			}
			if ( this.activeNode.cube.faceIsComplete(solvingFace)){
				System.out.println(humanStep+") Face. faceColor:"+solvingFace+ " crumbs:"+this.activeNode.breadcrumbs + " depth:"+this.activeNode.depth);
				System.out.println(this.activeNode.cube);
				this.search_depth =+ activeNode.depth + 6;
				this.correctSecondLevelEdges = this.activeNode.cube.getNumberOfSecondRowEdgeFilled(solvingFace);
				humanStep++;
			}
		}
		else if(humanStep == 2 && this.activeNode.cube.faceIsComplete(solvingFace)){
			if(correctSecondLevelEdges < this.activeNode.cube.getNumberOfSecondRowEdgeFilled(solvingFace)){
				this.queue.clear();
				this.queue.add(this.activeNode);
				correctSecondLevelEdges = this.activeNode.cube.getNumberOfSecondRowEdgeFilled(solvingFace);
				System.out.println(humanStep+") New 2nd Row Edge. faceColor:"+solvingFace+ " crumbs:"+this.activeNode.breadcrumbs + " depth:"+this.activeNode.depth);
				System.out.println(this.activeNode.cube);
			}
			if(this.activeNode.cube.faceHasSecondRow(solvingFace)){
				humanStep++;
				System.out.println(humanStep+") TwoRow. faceColor:"+solvingFace);
				System.out.println(this.activeNode.cube);
			}
		}
		if (humanStep < 1){
			stepSolveBFS();
		}else{
			stepSolveDFS();
		}
		if(humanStep > 2){
			
		}
	}
	
	public void solve(){
		while(!activeNode.cube.isSolved()){
			if(this.algorithm == ALGORITHM_BFS){
				stepSolveBFS();
			}else if(this.algorithm ==  ALGORITHM_DFS){
				stepSolveDFS();
			}else if(this.algorithm == ALGORITHM_HUMAN){
				stepHuman();
			}
		}
	}


	class CubeNode{
		
		Cube cube;
		
		String breadcrumbs;
		String move;
		int depth;
		CubeNode parent;
		HashMap<String, CubeNode> children;
		
		CubeNode(Cube cube){
			this.cube = cube;
			this.breadcrumbs = "";
			depth = 0;
		}
		
		CubeNode(CubeNode parent, String nextMove){
//			System.out.println("CHILD MOVE: "+nextMove+" \n"+childCube);
			this.cube = (Cube) DeepCopy.copy(parent.cube);
			this.cube.doMove(nextMove);
			this.move = nextMove;
			this.breadcrumbs = parent.breadcrumbs + nextMove + ",";
			this.parent = parent;
			this.depth = parent.depth + 1;
		}
		
		void defineChildren(){
			this.children = new HashMap<String, CubeNode>();
			for(String nextMove : Cube.POSSIBLE_MOVES){
				if (this.parent != null && this.parent.move != null && this.move != null &&
					this.parent.move.charAt(0) == nextMove.charAt(0) && 
					Cube.oppositeFaceColor(this.move.charAt(0)) == nextMove.charAt(0))
						continue;
				if (this.move != null && nextMove.charAt(0) == this.move.charAt(0))
					continue;
				CubeNode childNode = new CubeNode(this, nextMove);
				this.children.put(nextMove, childNode);
			}
		}
	}
	
}
