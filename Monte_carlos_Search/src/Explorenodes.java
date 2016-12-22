import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import pacman.controllers.Controller;
import pacman.controllers.examples.Legacy;
import pacman.controllers.examples.StarterPacMan;


import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;


public class Explorenodes {

	/**
	 * @param args
	 */;
	public int expandon=30;//when to expand
	StarterPacMan  pacmanstrategy;
	public int maximumSimulationLength=1000000;
	Stack<Game> states;
	 Game game;

	HashSet<Integer> activePowerPills;
		treenode rootnode;
		SelectionPolicy sp;
		Controller<EnumMap<GHOST,MOVE>> ghoststrategy;
	//	public ISelectionPolicy selectionPolicy; 
		
public Explorenodes(Game game1) {
	// TODO Auto-generated constructor stub
	//first time call from out controller
	game=game1;
pacmanstrategy=new StarterPacMan();
	rootnode =new treenode();
	states=new Stack<Game>();
	activePowerPills=new HashSet<Integer>();
	sp=new LevineUcb();
ghoststrategy=new Legacy();
	
	///later can also update the active pills
	int ind[]=game.getActivePillsIndices();
	for(int x:ind){
		activePowerPills.add(x);
	}
	
}

public void playmove(MOVE move) {
	// TODO Auto-generated method stub
	
	game.advanceGame(move, ghoststrategy.getMove(game,0));
	//get active pills 
	int ind[]=game.getActivePillsIndices();
	//update pills   doubt
	for(int x:ind){
		activePowerPills.add(x);
	}
	
	
}


public treenode bestnode (){
	
	treenode  bestnode=null;
	treenode whichnode=null;
	whichnode=rootnode;
	double max=Double.NEGATIVE_INFINITY;
	//use ghost postion
	//whichnode=whichnode.getChild(game);

	if(whichnode.getChildren()!=null){
	
		
		for(treenode child:whichnode.getChildren()){
			//System.out.println("yes111111"+" "+child.getAverageScore()+" "+child.getMove());
	if(child.getAverageScore()>max){
	
		
		max=child.getAverageScore();
		bestnode=child;
		
	}	
	
		}
	}
	
	return bestnode;
}


public Collection<treenode> getPacManChildren()
{
	Collection<treenode> children;
	
	//figure out which collection of children to return
	//if (parameters.useGhostPositions)
	//	children = rootnode.getChild(game).getChildren();
	//else
		children = rootnode.getChildren();
	
	//if there is no children, return an empty list instead of null
	if (children != null)
		return children;
	else
		return new ArrayList<treenode>();
}


public MOVE  getneutralbestmove(){
	
	
	MOVE bestmove=MOVE.NEUTRAL;
	double max=Double.MIN_VALUE;
	HashMap<MOVE, Double> chh=new HashMap<MOVE, Double>();
	
	if(rootnode.getChildren()!=null){
		
		for(treenode level1:rootnode.getChildren()){
			if(!level1.isleaf()){
				
			for(treenode level2:level1.getChildren()){
				Double already=chh.get(level2.getMove());
				if(already==null){
					
				chh.put(level2.getMove(), level2.getAverageScore());	
					
				}
				else{
					
					chh.put(level2.getMove(), already+level2.getAverageScore());	
					
				}
			};
			}
		}
	}
	
	
	for (Map.Entry<MOVE, Double> entry: chh.entrySet())
	{
		if (entry.getValue() > max)
		{
			max = entry.getValue();
			bestmove = entry.getKey();
		}
	}
	
	return bestmove;
}


public void setroot(Game game2) {
	// TODO Auto-generated method stub
	game=game2;
	
}
public void setroot(treenode root) {
	// TODO Auto-generated method stub
	rootnode=root;
	
}



public void run() {
	// TODO Auto-generated method stub
	
	Vector<treenode> markednode=new Vector<treenode>();

	int lives =game.getPacmanNumberOfLivesRemaining();
	
	
	////save the states for again getting the same state 
	states.add(game);
	game=game.copy();
	
	
	
	try{
		
	treenode  node =rootnode;
	markednode.add(node);
	
	//advanceGameToNextNode();
	//System.out.println("sdvsdvsdv");
	//advance the game
	///if(use ghost positions )
	//node =node.getChild(game);
	while(node.isleaf()==false){
		
		//System.out.println(node);

		
		node=sp.Select(node);
		//System.out.println(node);
		//node=selectionalgo.selectbestucbchild   
		//if game over returns
		if(node==null){
			System.out.println("www");
			return;
			
			
		}
		
		markednode.add(node);
		//node.numberOfVisits++;
		///node.getmove gives the move which lead to this node 
		playmove(node.getMove());
		//advance game further
	//	advanceGameToNextNode();
		//use ghost also
		
		
		
	//	node=node.getChild(game);
	
		
		
	}
	
	
	//only one node ie root node or in simulation we have sampled it enough number of time then expand it
	

//	System.out.println(node.getnumberofvisit());
	
	if(node.getnumberofvisit()>=expandon ||node==rootnode)
	{	;node.expand(game);
		//////evaluate all childs after expanding do the following that is evaluate childrens 
		System.out.println("//yes--------------------------------");
		//node.getchildren return all child on which we will now do estimation 
		for(treenode child:node.getChildren()){
			int cmpowerpills=game.getNumberOfActivePowerPills();
			int anypill=game.getNumberOfActivePills();
			int level =game.getCurrentLevel();
			
			states.push(game);
			game=game.copy();
			
			playmove(child.getMove());
			//advance game;
	//		advanceGameToNextNode();
			
			if(cmpowerpills>game.getNumberOfActivePowerPills()){
				child.setactivepowerpill(true);
				child.addscorebonus(200);
			}
			

			if(anypill>game.getNumberOfActivePills()){
				child.setanypill(true);
				child.addscorebonus(100);
			}
			
int points =0;
			if(game.getCurrentLevel()>level){
			points=points +3000;
			}
			//use ghost pos 
			
			//run further simuations on these childs 
			points =points +runlocalsimulations(markednode,lives);
			
			child.updatescores(points);
			//tell child about updates
			
			game=states.pop();
			
			
			
			
			
			
			
			
			
		}
		node=sp.Select(node);
		if(node==null)return;
		markednode.add(node);
		playmove(node.getMove());
		
		
		//use ghost
	//	node=node.getChild(game);
		
	}	
	
	runlocalsimulations(markednode,lives);
	///node=selectbypolicy;
	//now this node will also becomne a part of our local  simulation before running it on 

	
	

	
	
		
	}
finally{
	game=states.pop();
	
		//System.out.println("hero");
	
}	
	
	//even in case it return on becoming null it should return 
	
}

private int runlocalsimulations(Vector<treenode> markednode, int lives) {
	// TODO Auto-generated method stub
	
	int sc=0;
	int deathPenalty=10000;
	if(game.getPacmanNumberOfLivesRemaining()<lives)sc=sc-deathPenalty;
	
	
	sc=sc+roolout();
	
	for(treenode child:markednode){
		child.updatescores(sc);
		//System.out.println("see"+" "+sc+" "+System.currentTimeMillis());
	}
	return sc;
}

private int roolout() {
	// TODO Auto-generated method stub
	
	
	int level=game.getCurrentLevel();
	int le=0;
	while(le<100&&game.gameOver()==false&&game.getCurrentLevel()==level){
		le++;
		
		game.advanceGame(pacmanstrategy.getMove(game, 0), ghoststrategy.getMove(game, 0));
	}
	return game.getScore();
	
	
}



/**
 * Advances the game by playing the move specified for Ms. Pac-Man.
 * @param node
 */


/**
 * Determines if the current game position is a node in the graph; i.e., if it is a Pac-Man decision point (or game over).
 * @return
 */




/**
 * Determines if Ms. Pac-Man has ran into a wall based on the direction she was previously going in.
 * @return
 */



public Game getGameState()
{
	return game;
}


/**
 * Sets the current game state.
 * @param value
 */
public void setGameState(Game value)
{
	game = value;
}


/**
 * Gets the root node of the search tree.
 * @return
 */
public treenode getRootNode()
{
	return rootnode;
}


/**
 * Sets the root node of the tree.
 * @param node
 */
public void setRootNode(treenode node)
{
	rootnode = node;
}





}






class treenode {
	
	
	private MOVE move;
	int totalScore, numberOfVisits, scoreBonus;//for ucb and samping threshold
	private treenode  parent;
	private Map<Object, treenode > children;
	private int nodeIndex;//current position of pacman when reaches this node 
	private long sumOfSquares;
	private double mean;
	private boolean moveEatsPowerPill;
	private boolean moveEatsPills;
	private long ghostPositions;

	/**
	 * Constructor for root nodes.
	 */
	public treenode ()
	{
		totalScore = 0;
		numberOfVisits = 1;
		move = MOVE.NEUTRAL;
		nodeIndex = -1;
		scoreBonus = 0;
	}
	
	private treenode (treenode parent, MOVE move)
	{
		this();
		this.parent = parent;
		this.move = move;
	}

	public boolean isleaf() {
		// TODO Auto-generated method stub
		if(children==null)return true;
		return false;
	}

	public void updatescores(int points) {
		// TODO Auto-generated method stub
		
		
		totalScore=totalScore+points;
		numberOfVisits++;
		if(numberOfVisits==1){
			mean =points ;
			sumOfSquares=0;
		}
		else {
		double lastmean =mean;
			mean =mean+(points-mean)/numberOfVisits;
			//auto type cast
			sumOfSquares+=(points-lastmean)*(points-mean);
		}
		
		
	}
public void addscorebonus(int bonus){
	scoreBonus+=bonus;
}



	public void setanypill(boolean b) {
		// TODO Auto-generated method stub

		
		this.moveEatsPills=b;
		
	}

	public void setactivepowerpill(boolean b) {
		// TODO Auto-generated method stub
		this.moveEatsPowerPill=b;
	}

	public Collection<treenode> getChildren() {
		// TODO Auto-generated method stub
		
		if(children==null)return null;
		return children.values();
	}

	public void expand(Game game) {
		// TODO Auto-generated method stub
		MOVE poss[]=game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
		children=new HashMap<Object,treenode>(poss.length);
		for(int i=0;i<poss.length;i++){
		//	System.out.println("dgbdgb"+" "+poss[i]);
			children.put(poss[i], new treenode(this,poss[i]));
		}
		
	}
	public int getNodeIndex()
	{
		return nodeIndex;
	}


	/**
	 * Gets the parent node for this node.
	 * @return
	 */
	public treenode getParent()
	{
		return parent;
	}


	/**
	 * Gets the total score for the node.
	 * @return
	 */
	public int getTotalScore()
	{
		return totalScore;
	}
	public double getAverageScore()
	{
		if (numberOfVisits > 0)
			return mean + scoreBonus;
		else
			return scoreBonus;
	}

	public double getVariance()
	{
		if (numberOfVisits > 1)
			return sumOfSquares / (numberOfVisits - 1);
		else
			return 0;
	}
	
	public boolean getactivepowerpill(){
		return this.moveEatsPowerPill;
	}

	public boolean getanypill(){
		return this.moveEatsPills;
	}
	
	public boolean caneatonnextmove(){
		if(children==null)return false;
		for(treenode child:children.values()){
		if(child.getanypill()){
			return true;
		}	
		}
		return false;
	}
	
	public MOVE getMove() {
		// TODO Auto-generated method stub
		return move;
	}

	
	public treenode getChild(MOVE move) {
		// TODO Auto-generated method stub
		return 	children.get(move);
		
	}
	
	
	private long getGhostPositions(Game game)
	{
		long positions;
		positions = game.getGhostCurrentNodeIndex(GHOST.BLINKY);
		positions <<= 16;
		positions |= game.getGhostCurrentNodeIndex(GHOST.INKY);
		positions <<= 16;
		positions |= game.getGhostCurrentNodeIndex(GHOST.PINKY);
		positions <<= 16;
		positions |= game.getGhostCurrentNodeIndex(GHOST.SUE);
		return positions;
	}


	/**
	 * Gets the ghost positions stored for this node.
	 * @return
	 */
	public long getGhostPositions()
	{
		return ghostPositions;
	}

	/**
	 * Sets the ghost positions to the specified value.
	 * @param value
	 */
	public void setGhostPositions(long value)
	{
		ghostPositions = value;
	}

	public double getnumberofvisit() {
		// TODO Auto-generated method stub
		return numberOfVisits;
	}

	
	
	
	
	
}