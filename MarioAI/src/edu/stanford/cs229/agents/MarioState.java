package edu.stanford.cs229.agents;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;

//Each MarioState Instance is an abstract representation of the game environment
public class MarioState {
	//Constructor
	public MarioState(Environment environment){
		this.marioMode = environment.getMarioMode();
		this.enemyPresent = findEnemies(environment);
	}
	
	//0-small, 1-big, 2-fire
	int marioMode;
	
	//high-front, high-back, low-front, low-back
	private boolean[] enemyPresent;
	
	//whether there is brick/question nearby
	private boolean breakableNear;
	//
	private boolean mayUpgrade;
	private boolean marioNearGap;
	private boolean marioOnGap;
	
/*	This attribute is 4-ary-valued, 0 
	represents the nearest enemy doesn’t fear stomp and 
	fireball. 1 represents the nearest enemy could be 
	stomped. 2 represents the nearest enemy could beat 
	by fireball. 3 represents the nearest enemy could 
	beat by stomping and fireball.*/ 
	private int enemyType;
	
	
	
	// Returns 4 booleans representing whether:
	// high-front has enemy
	// high-back has enemy
	// low-front has enemy
	// low-back has enemy
	boolean[] findEnemies(Environment environment){
		boolean [] enemyList = {false, false, false, false};
		//TODO: finish implementing this
		return enemyList;
	}
	
	// Return a unique number for each different state
	int getStateNumber(){
		//TODO: implement this, return a concatenation of the booleans
		return 0;
	}
	
	void update(Environment environment){
		//incorporate observation
//	    levelScene = environment.getLevelSceneObservationZ(zLevelScene);
//	    enemies = environment.getEnemiesObservationZ(zLevelEnemies);
//	    mergedObservation = environment.getMergedObservationZZ(1, 0);
//
//	    this.marioFloatPos = environment.getMarioFloatPos();
//	    this.enemiesFloatPos = environment.getEnemiesFloatPos();
//	    this.marioState = environment.getMarioState();
//
//	    receptiveFieldWidth = environment.getReceptiveFieldWidth();
//	    receptiveFieldHeight = environment.getReceptiveFieldHeight();
//
//	    // It also possible to use direct methods from Environment interface.
//	    //
//	    marioStatus = marioState[0];
//	    marioMode = marioState[1];
//	    isMarioOnGround = marioState[2] == 1;
//	    isMarioAbleToJump = marioState[3] == 1;
//	    isMarioAbleToShoot = marioState[4] == 1;
//	    isMarioCarrying = marioState[5] == 1;
//	    getKillsTotal = marioState[6];
//	    getKillsByFire = marioState[7];
//	    getKillsByStomp = marioState[8];
//	    getKillsByShell = marioState[9];
	}
	
}
