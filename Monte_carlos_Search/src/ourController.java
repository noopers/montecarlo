import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.controllers.examples.Legacy;
import pacman.controllers.examples.StarterPacMan;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;


public class ourController  extends Controller<MOVE>{
//number of time simulations runs for
	
public int deathPenalty=100000;
	
public int levelcompletebonus;
	boolean discardtree=true;
public Controller<EnumMap<GHOST, MOVE>> ghoststrategy;
public Controller<MOVE> pacmanstrategy;

public int simulatecount=-1 ;//how many time if -1 as much as possible

public boolean ghostuse=true;
public boolean eatghost=true;

public Explorenodes explore;
private int lastEdibleScore;
ITreeEvaluator[] additionalEvaluators;
public ourController() {
	// TODO Auto-generated constructor stub
	ghoststrategy=new Legacy();
	pacmanstrategy=new StarterPacMan();
	additionalEvaluators = new ITreeEvaluator[] { new DistanceToOpportunityEvaluator(), new PowerPillDistanceEvaluator(), new PowerPillActiveEvaluator() };
}

//public ISelectionPolicy selectionPolicy;  //node selecting techinque
	//lastedible//timedue

	@Override
	
	
	
	// one  parameter is time to make move
	public MOVE getMove(Game game, long timetomakemove) {
		// TODO Auto-generated method stub
	
		//System.out.println(timetomakemove);
		
		MOVE move=MOVE.NEUTRAL;
		
		
		
		
		
		//111111111111111111111111111111
		if(explore==null){
			
			//use builtin jar method to find possible move;
			int currpacmanpos=game.getPacmanCurrentNodeIndex();
			MOVE arr[]=game.getPossibleMoves(currpacmanpos);
	        int mv=(int)(Math.random()*arr.length);
            move=arr[mv];
            lastEdibleScore=game.getGhostCurrentEdibleScore();
            //now start making tree with this node as root 
	    explore=new Explorenodes(game);
	
	
	//// now tell tree we are making our first move 
	
	   explore.playmove(move);
			
			
			
		}
		
		else{
			//now make the game state as rootnode or decision node
			explore.setroot(game);
			
			
			
		}
		
		
		
		
		
		
		
		
		///////////////////22222222222
		
		
			timetomakemove-=20;
		
		
		
		int cn=0;
		
		if(simulatecount==-1){
			
			long t=System.currentTimeMillis();
			while(cn<10){
				
				//run simulation  on this current state to se as far as possible on the basis of average values
				cn++;
				explore.run();
			
			}
		}
	//	System.out.println(cn);
//System.out.println(		System.currentTimeMillis()+" "+timetomakemove);
		
		if(move==MOVE.NEUTRAL){
			
			System.out.println("///////////////////////////////////////////");	
			//run till simulation count;
			
			
		runAdditionalEvaluators();
			treenode bestnode;
			bestnode=explore.bestnode();
			if(bestnode==null&&ghostuse){
				//doubt
			//	move=explore.getneutralbestmove();
			}
			else{
			//	System.out.println("wow");
				
				move=bestnode.getMove();
			}
			
			//discard tree
			if(discardtree||bestnode==null){
				explore=new Explorenodes(game);
			}
			else{
				explore.setroot(bestnode);
			}
			
			
			
			
			
		}
		
		//System.out.println(		System.currentTimeMillis()+" "+timetomakemove+" mmm");
		if (System.currentTimeMillis() > timetomakemove)
		{
			//oops, too late: increase the time buffer so we're not late next time
			//timeBuffer++;
			System.out.println("Too late!");
		}
		
		lastEdibleScore=game.getGhostCurrentEdibleScore();
	/*	
		if(move==MOVE.NEUTRAL&&Math.random()<0.2){
			int currpacmanpos=game.getPacmanCurrentNodeIndex();
			MOVE arr[]=game.getPossibleMoves(currpacmanpos);
	        int mv=(int)(Math.random()*arr.length);
            move=arr[mv];
            lastEdibleScore=game.getGhostCurrentEdibleScore();
		}*/
		return move;
	}

	private void runAdditionalEvaluators()
	{
		
			for (ITreeEvaluator evaluator: additionalEvaluators)
			{
				evaluator.evaluateTree(explore);
			}
		
	}
	

}
//run evaluator