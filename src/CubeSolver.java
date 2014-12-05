
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import FastCopy.DeepCopy;

public class CubeSolver {

	int MAX_DEPTH = 6;
	static final int ALGORITHM_DFS = 0;
	static final int ALGORITHM_BFS = 1;
	static final int ALGORITHM_HUMAN = 2;
	static final int ALGORITHM_GENETIC = 3;
	
	int algorithm;
	int search_depth = -1;
	CubeNode activeNode;
	LinkedList<CubeNode> queue;
	
	CubeSolver(Cube cube){
		initWithCube(cube);
		MAX_DEPTH = cube.moveCount;
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
		if(activeNode.depth >= MAX_DEPTH || (search_depth > 0 && activeNode.depth >= search_depth )){
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
		if(this.algorithm == ALGORITHM_GENETIC){
			this.solveGeneticly(this.activeNode.cube);
		}else{
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
		
	}
	
	public void solveGeneticly(Cube scrambledCube){
		
//		String startSequence = "F3,U,R,L2,F,D2,B3";
		String startSequence = Cube.generateMoveSequence(scrambledCube.moveCount);
		System.out.println("cubeScore:"+scrambledCube.score());
		System.out.println("startSequence:"+startSequence);

		//a reusable random
		Random rand = new Random();
		
		//create a data store.
		ArrayList<GeneticNode> data = new ArrayList<GeneticNode>();
		
		for(int i=0; i<100; i++){
			//add start_sequence to the hashMap with score:0, mutation:0;
			data.add(new GeneticNode(startSequence));
		}
		int highScore = 0;
		System.out.println(String.format("%-11s| %-9s | %-8s | %-30s | %-9s | %-8s | %s","Generation", "HighScore", "BestNode", "BestSequence", "ZeroScores", "AvgScore", "HighScorers"));
		for(int generation=0; generation>=0; generation++){

			//create list-of-favorable-sequences.
			ArrayList<GeneticNode> listOfFavorableSequences = new ArrayList<GeneticNode>();

			//reset zero score nodes.
//			for(int i=0; i<data.size(); i++){
//				GeneticNode node = data.get(i);
//				if(node.score < 6){
//					GeneticNode newNode = new GeneticNode(startSequence);
//					data.set(i, newNode);
//				}
//			}
			
			//for each sequence [key] in hashMap:
			int zeroScoreNodes = 0;
			int newHighScore = 0;
			int scoreTotal = 0;
			for (GeneticNode node : data){
				//mutate the sequence.
				node.mutateSequence();
				//run the mutated sequence on a new cube.
				Cube cube = (Cube) DeepCopy.copy(scrambledCube);
				cube.doMoves(node.sequence);

				//set the mutation's score.
				node.setScore(cube.score());
				
				scoreTotal+=node.score;
				
				if(node.score < 6){
					zeroScoreNodes++;
				}
				if(node.score == Cube.SCORE_MAX){
					System.out.println("Solution: "+node.sequence);
					System.exit(1);
				}
				if (node.score > newHighScore){
					newHighScore = node.score;
				}
				if(node.mutationHelped){
					listOfFavorableSequences.add(node);
				}
			}
			Collections.sort(data);
			//apply good mutations to poor scoring nodes.
			for(int a = 0; a<listOfFavorableSequences.size(); a++){
				GeneticNode goodNode = listOfFavorableSequences.get(a);
				GeneticNode badNode = data.get(a);
				badNode.mutateSequence(goodNode.mutationIndex, goodNode.mutationMove);
			}
			
			if(newHighScore > highScore){
				GeneticNode bestNode = data.get(data.size()-1);
				GeneticNode worstNode = data.get(0);
				highScore = bestNode.score;
				System.out.println(String.format("%-11d| %-9d | %-8s | %-30s | %-10d | %-8s | %s", generation, bestNode.score, bestNode.id, bestNode.sequence, zeroScoreNodes, scoreTotal/data.size(), data.subList(0, 20)));
			}
			
			//print generation and best sequence / score.
		}
	}
	
	public class GeneticNode implements Comparable<GeneticNode>{
		public int id;
		public Boolean mutationHelped;
		public int score;
		public int mutationIndex;
		public String mutationMove;
		public String sequence;
		
		public GeneticNode(String startSequence) {
			this.id = (new Random()).nextInt(1000000);
			this.sequence = startSequence;
		}

		public void setScore(int score){
			mutationHelped = false;
			if(this.score < score) mutationHelped = true;
			this.score = score;
		}
		
		/*
		 * Creates a random mutation in this node's sequence **/
		public void mutateSequence() {
			//generate random sequence mutation
			String[] moves = this.sequence.split(",");
			Random rand = new Random();
			this.mutationIndex = rand.nextInt(moves.length-1);
			this.mutationMove = Cube.POSSIBLE_MOVES[rand.nextInt(Cube.POSSIBLE_MOVES.length-1)];
			mutateSequence(this.mutationIndex, this.mutationMove);
		}

		/*
		 * Applies a specific mutation in this node's sequence **/
		public void mutateSequence(int mutationIndex, String mutationMove){
			String[] moves = this.sequence.split(",");
			moves[mutationIndex] = mutationMove;
			String newSequence = StringUtils.join(moves,",");
//			System.out.println("mutated:"+this.sequence + " to:"+newSequence);
			this.sequence = newSequence;
		}

		@Override
		public int compareTo(GeneticNode o) {
			if(this.score < o.score) return -1;
			if(this.score > o.score) return 1;
			return 0;
		}
		
		public String toString(){
			return this.id+"";
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
