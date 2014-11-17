
import java.util.ArrayList;

import FastCopy.DeepCopy;

public class CubeSolver {

	static final int MAX_DEPTH = 20;
	
	int activeDepth;
	CubeNode activeNode;
	CubeNode root;
	
	CubeSolver(Cube cube){
		this.activeDepth = 0;
		this.root = new CubeNode(cube);
		this.activeNode = this.root;
	}
	
	public boolean solveBFS(){
		while(!activeNode.hasSolvedChild()){
			int numChildren = this.activeNode.children.size();
			this.activeNode = this.activeNode.children.get(numChildren-1);
			this.activeDepth++;
			if(activeDepth == MAX_DEPTH){
				this.activeNode.children.remove(this.activeNode.children.size()-1);
			}
		}
		return false;
	}
	
	
	class CubeNode{
		
		CubeNode parent;
		Cube cube;
		ArrayList<CubeNode> children;
		
		CubeNode(Cube cube){
			this.cube = cube;
		}
		
		void setParent(CubeNode parent){
			this.parent = parent; 
		}
		
		Boolean hasSolvedChild(){
			if(this.children == null){
				this.exploreChildren();
			}
			for(CubeNode child : this.children){
				if (child.cube.isSolved()) return true;
			}
			return false;
		}
		
		void exploreChildren(){
			this.children = new ArrayList<CubeNode>();
			for(int faceIndex=0; faceIndex<Cube.FACE_ORDER.length(); faceIndex++){
				Cube childCube = (Cube) DeepCopy.copy(cube);
				String nextMove = Cube.FACE_ORDER.charAt(faceIndex)+"";
				childCube.doMove(nextMove);
				this.children.add(new CubeNode(childCube));
			}
		}
		
		
	}
	
}
