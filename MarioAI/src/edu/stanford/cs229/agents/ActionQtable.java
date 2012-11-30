package edu.stanford.cs229.agents;

import java.util.Arrays;


/* Stores and picks best action for mario advancing right
 * 
 * */
public class ActionQtable extends Qtable{

	float explorationChance=0.4f;
	
	ActionQtable(int actionRange) {
		super(actionRange);
	}
	
	
	int getBestAction(int stateNumber){
		float[] rewards = this.getActionsQValues(stateNumber);
		if (rewards.length == 0){
			System.out.println("No rewards defined for this state");
			return 0;
		}
		else {
			//System.out.println("Rewards for this state");
			///System.out.println(Arrays.toString(rewards));
			float maxRewards = Float.NEGATIVE_INFINITY;
			int indexMaxRewards = 0;
			
			for (int i = 0; i < rewards.length ; i++){
				System.out.println(rewards[i]);
				if (maxRewards < rewards[i]) {
					maxRewards = rewards[i];
					indexMaxRewards = i;
				}
			}
			System.out.println(rewards.length);
			return indexMaxRewards;
		}
	}

	
	
	void updateQvalue(int reward, int currentStateNumber){
        //TODO: Implement this
        //Q(prevState, prevAction) = alpha * Qprev + (1-alpha) * reward 
        //                                        + gamma * maxQ)
        int bestAction = getBestAction(currentStateNumber);
        float maxQ = getActionsQValues(currentStateNumber)[bestAction];
        float[] prevQs = getActionsQValues(prevState);
        float prevQ = prevQs[prevAction];
        float newQ = learningRate * prevQ +
                     (1 - learningRate) * (reward + gammaValue * maxQ);
        prevQs[prevAction] = newQ;
        table.put(prevState, prevQs);
        
    }

	//set the Q values
	public void initialize() {
		int numActions = 32;
		float[] initValues = {1};
		//insert q values for all valid states
		this.table.put(0, initValues);
		
	}

	
}
