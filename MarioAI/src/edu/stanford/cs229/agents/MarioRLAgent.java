package edu.stanford.cs229.agents;

import ch.idsia.agents.Agent;
import ch.idsia.agents.LearningAgent;
//import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.tasks.LearningTask;


public class MarioRLAgent implements LearningAgent
{
	//Fields for the Mario Agent
	public MarioState currentState;
	public int currentTask;
	
	//Associated Qtable for the agent. Used for RL training
	public Qtable table;
	
	//Enumerate denote what stage the Agent is in
	//init: initial phase
	//learn: accumulatively update the Qtable
	enum phase {INIT, LEARN};
	phase currentPhase;
	
	public boolean action[] = new boolean[Environment.numberOfKeys];
	public String name = "Instance_of_MarioRLAgent._Change_this_name";

	/*final*/
	public byte[][] levelScene;
	/*final */
	public byte[][] enemies;
	public byte[][] mergedObservation;

	public float[] marioFloatPos = null;
	public float[] enemiesFloatPos = null;

	public int[] marioState = null;

	public int marioStatus;
	public int marioMode;
	public boolean isMarioOnGround;
	public boolean isMarioAbleToJump;
	public boolean isMarioAbleToShoot;
	public boolean isMarioCarrying;
	public int getKillsTotal;
	public int getKillsByFire;
	public int getKillsByStomp;
	public int getKillsByShell;

	public int receptiveFieldWidth;
	public int receptiveFieldHeight;
	public int marioEgoRow;
	public int marioEgoCol;

	// values of these variables could be changed during the Agent-Environment interaction.
	// Use them to get more detailed or less detailed description of the level.
	// for information see documentation for the benchmark <link: marioai.org/marioaibenchmark/zLevels
	int zLevelScene = 1;
	int zLevelEnemies = 0;
	
	//Constructor
	public MarioRLAgent(String s){
		setName(s);
	}
	
	
	@Override
	//calculate the keystrokes based on current task
	//also updates the current task field
	public boolean[] getAction() {
		// TODO Auto-generated method stub
		//action[Mario.KEY_SPEED] = action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
	    
		int task = table.getBestAction(currentState.getStateNumber());
		currentTask = task;
		return bestAction(task);
	}

	@Override
	//read the environment as state?
	public void integrateObservation(Environment environment) {
		// TODO Auto-generated method stub
	    //update the current state
	    currentState.update(environment);
	    
	    
	}

	@Override
	public void giveIntermediateReward(float intermediateReward) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	    action = new boolean[Environment.numberOfKeys];
	    action[Mario.KEY_RIGHT] = true;
	    action[Mario.KEY_SPEED] = true;
	}

	@Override
	public void setObservationDetails(int rfWidth, int rfHeight, int egoRow,
			int egoCol) {
		// TODO Auto-generated method stub
		receptiveFieldWidth = rfWidth;
	    receptiveFieldHeight = rfHeight;

	    marioEgoRow = egoRow;
	    marioEgoCol = egoCol;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
/* Variables*/
// we need variables to represent: state representation, current state, big 
// reward table for (state, action) pairs
	
/* Methods */

	// Transform the environment to a state representation
	// state = getState(Environment);
	
	// Calculate the optimal action for this state
	boolean[] bestAction(int taskNumber){
		return null;
	}


	@Override
	public void learn() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void giveReward(float reward) {
		// TODO Auto-generated method stub
		
	}


	@Override
	//The first episode run initializes the agent.
	public void newEpisode() {
		// TODO Auto-generated method stub
		switch(currentPhase){
		case INIT:
			break;
		}
	}


	@Override
	public void setLearningTask(LearningTask learningTask) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setEvaluationQuota(long num) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Agent getBestAgent() {
		// TODO Auto-generated method stub
		return this;
	}


	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
