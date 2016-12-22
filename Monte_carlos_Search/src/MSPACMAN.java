

import java.util.Random;

import pacman.Executor;
import pacman.controllers.HumanController;
import pacman.controllers.KeyBoardInput;
import pacman.controllers.examples.Legacy;
import pacman.controllers.examples.StarterGhosts;
import pacman.controllers.examples.StarterPacMan;
import pacman.game.*;
public class MSPACMAN {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Random r=new Random();
		
		
		Executor e = new Executor();
		
		//acts as an entry point in the game        
		//starterpacman(),legacy() is the basic technique supported by jar files we replace it with ourController();  
		
		//  new humanController(new keyBoardInput());
		
		e.runGameTimed(new ourController(), new Legacy(),true);
	
		
		
	
		
		
		
		
	}

}
