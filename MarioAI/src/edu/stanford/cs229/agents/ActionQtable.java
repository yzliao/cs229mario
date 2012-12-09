package edu.stanford.cs229.agents;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Random;

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
    if (rewards == null) {
      System.err.println("No rewards defined for this state");
      return 0;
    } else {
      // System.out.println("Rewards for this state");
      // System.out.println(Arrays.toString(rewards));
      float maxRewards = Float.NEGATIVE_INFINITY;
      int indexMaxRewards = 0;

      if (LearningParams.DEBUG) {
        //System.out.print("Q values:");
      }
      for (int i = 0; i < rewards.length; i++) {
        //System.out.println(rewards[i]);
        if (maxRewards < rewards[i]) {
          maxRewards = rewards[i];
          indexMaxRewards = i;
        }
        if (LearningParams.DEBUG) {
          if (i > 0) {
            //System.out.print(", ");
          }
          //System.out.print(rewards[i]);
        }
      }
      if (LearningParams.DEBUG) {
        //System.out.println();
      }
      
      if (LearningParams.DEBUG) {
        //System.out.println("Best action: " + indexMaxRewards);
      }
      //System.out.println(rewards.length);
      return indexMaxRewards;
    }
  }
  
  @Override
  void updateQvalue(int reward, int currentStateNumber) {
    transitions.addTransition(prevState, prevAction, currentStateNumber);
    
    // Update Q values using the following update rule:
    //
    // Q(prevState, prevAction) =
    //     (1 - alpha) * Qprev + alpha * (reward + gamma * maxQ)
    //
    // where alpha = learningRate / # prevState/prevAction visited.
    
    boolean shouldPrint = prevState == 1026 && prevAction == 2 && false;
    if (shouldPrint) {
      System.out.println("++++++++++++++++++++++");
      System.out.println(
          "current: " + currentStateNumber + " reward: " + reward);
    }
    
    int bestAction = getBestAction(currentStateNumber);
    float maxQ = getActionsQValues(currentStateNumber)[bestAction];

    float[] prevQs = getActionsQValues(prevState);
    float prevQ = prevQs[prevAction];
    
    float alpha =
        LearningParams.BASE_LEARNING_RATE +
        learningRate / transitions.getCount(prevState, prevAction);
    
    if (LearningParams.DEBUG) {
      System.out.println(
        "count: " + prevState + ", " + prevAction + " = " + transitions.getCount(prevState, prevAction));
    }
    
    float newQ = (1 - alpha) * prevQ +  alpha * (reward + gammaValue * maxQ);
    
    prevQs[prevAction] = newQ;
    
    if (shouldPrint) {
      System.out.println("prevQ: " + prevQ + " maxQ: " + maxQ + " newQ: " + newQ);
    }
  }
  
  @Override
  float[] getInitialQvalues(int stateNumber) {
    float[] initialQvalues = new float[actionRange];
    for (int i = 0; i < actionRange; i++) {
      // Set as random float ranged (-.1, .1), check whether makes sense.
      initialQvalues[i] = (float) (randomGenerator.nextFloat() * 0.2 - 0.1);
    }
    return initialQvalues;
  }

  public void dumpQtable(String logfile) {
    System.out.println("** Dumping Qtable to " + logfile + " **\n");
    
    StringBuilder sb = new StringBuilder();
    // Find the size of qtable.
    for (int key : getTable().keySet()){
      sb.append(printState(key));
    }
    
    byte data[] = sb.toString().getBytes();

    try {
      OutputStream out = new FileOutputStream(logfile);
      out.write(data, 0, data.length);
    } catch (IOException x) {
      System.err.println(x);
    }
  }
  
  public String printState(int key) {
    StringBuilder sb = new StringBuilder(
        String.format("%6d %s:", key, MarioState.printStateNumber(key)));
    for (float v : getTable().get(key)) {
      sb.append(String.format(" %.4f", v));
    }
    sb.append("\n");
    return sb.toString();
  }
}
