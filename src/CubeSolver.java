
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import FastCopy.DeepCopy;

public class CubeSolver {

	static final int MAX_DEPTH = 10;
	
	String algorithm;
	
	CubeNode activeNode;
	LinkedList<CubeNode> queue;
	
	CubeSolver(Cube cube){
		initWithCube(cube);
	}
	
	CubeSolver(Cube cube, String algorithm){
		initWithCube(cube);
		this.algorithm = algorithm;
	}
	
	private void initWithCube(Cube cube){
		this.activeNode =  new CubeNode(cube);
		queue = new LinkedList<CubeNode>();
		queue.add(this.activeNode);
		this.algorithm = "BFS";
	}

	private void stepSolveBFS(){
		activeNode = queue.pop();
		if(activeNode.cube.isSolved())
			return;
		if( activeNode.depth < CubeSolver.MAX_DEPTH){
			activeNode.defineChildren();
			Iterator<CubeNode> entries = activeNode.children.values().iterator();
			while (entries.hasNext()) {
				queue.add(entries.next());
			}
		}
	}
	
	private void stepSolveDFS(){
		if(activeNode.cube.isSolved())
			return;
		if(activeNode.depth >= CubeSolver.MAX_DEPTH){
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
	
	public void solve(){
		int step = 0;
		while(!activeNode.cube.isSolved()){
			if(this.algorithm == "BFS"){
				stepSolveBFS();
			}else if(this.algorithm == "DFS"){
				stepSolveDFS();
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
				if (this.move != null && nextMove.charAt(0) == this.move.charAt(0))
					continue;
				CubeNode childNode = new CubeNode(this, nextMove);
				this.children.put(nextMove, childNode);
			}
		}
		
		
	}
	
}
