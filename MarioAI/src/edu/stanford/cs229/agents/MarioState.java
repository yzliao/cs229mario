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
	
	private int marioMode;
	
	//high-front, high-back, low-front, low-back
	private boolean[] enemyPresent;
	
//	private boolean highEnemyOnFront;
//	private boolean highEnemyOnBack;
//	private boolean lowEnemyOnFront;
//	private boolean lowEnemyOnBack;
	
	private boolean breakableNear;
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
	private boolean[] findEnemies(Environment environment){
		boolean [] enemyList = {false, false, false, false};
		//TODO: finish implementing this
		return enemyList;
	}
	
	// Return a unique number for each different state
	private int getStateNumber(){
		//TODO: implement this, return a concatenation of the booleans
		return 0;
	}
	
}
