package edu.stanford.cs229.agents;

import java.util.Arrays;
import java.util.Hashtable;

/* Stores and picks best action for mario advancing right
 * 
 * */
public class ActionQtable extends Qtable {

  TransitionTable transitions = new TransitionTable();

  ActionQtable(int actionRange) {
    super(actionRange);
  }

  @Override
  int getBestAction(int stateNumber) {
    float[] rewards = this.getActionsQValues(stateNumber);
    if (rewards.length == 0) {
      System.out.println("No rewards defined for this state");
      return 0;
    } else {
      // System.out.println("Rewards for this state");
      // System.out.println(Arrays.toString(rewards));
      float maxRewards = Float.NEGATIVE_INFINITY;
      int indexMaxRewards = 0;

      for (int i = 0; i < rewards.length; i++) {
        //System.out.println(rewards[i]);
        if (maxRewards < rewards[i]) {
          maxRewards = rewards[i];
          indexMaxRewards = i;
        }
      }
      //System.out.println(rewards.length);
      return indexMaxRewards;
    }
  }
}
