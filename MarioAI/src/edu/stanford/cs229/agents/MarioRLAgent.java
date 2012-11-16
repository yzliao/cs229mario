package edu.stanford.cs229.agents;

import ch.idsia.agents.Agent;
import ch.idsia.agents.LearningAgent;
import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.tasks.LearningTask;


public class MarioRLAgent implements LearningAgent
{

	
	@Override
	public boolean[] getAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void integrateObservation(Environment environment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void giveIntermediateReward(float intermediateReward) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObservationDetails(int rfWidth, int rfHeight, int egoRow,
			int egoCol) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
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
	public void newEpisode() {
		// TODO Auto-generated method stub
		
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
		return null;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
/* Variables*/
// we need variables to represent: state representation, current state, big 
// reward table for (state, action) pairs
	
/* Methods */

	// Transform the environment to a state representation
	// state = getState(Environment);
	
	// Calculate the optimal action for this state
	// action = getOptimalAction(state);
	



// Q leaning algorithm
// actions = getAvailableActions(state);


}
