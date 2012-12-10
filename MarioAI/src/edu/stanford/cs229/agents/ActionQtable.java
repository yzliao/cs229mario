package edu.stanford.cs229.agents;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/* Stores and picks best action for mario advancing right
 * 
 * */
public class ActionQtable extends Qtable {

  TransitionTable transitions;

  ActionQtable(int actionRange) {
    super(actionRange);
    transitions = new TransitionTable(actionRange);
  }

  @Override
  int getBestAction(long stateNumber) {
    float[] rewards = this.getActionsQValues(stateNumber);
    if (rewards == null) {
      System.err.println("No rewards defined for this state");
      return 0;
    } else {
      float maxRewards = Float.NEGATIVE_INFINITY;
      int indexMaxRewards = 0;

      Logger.print(4, "Q values: ");

      for (int i = 0; i < rewards.length; i++) {
        //Logger.println(3, rewards[i]);
        if (maxRewards < rewards[i]) {
          maxRewards = rewards[i];
          indexMaxRewards = i;
        }

        Logger.print(4, (i > 0 ? ", " : "") + rewards[i]);
      }
      
      Logger.println(4, "\nBest action: " + indexMaxRewards);
      return indexMaxRewards;
    }
  }
  
  @Override
  void updateQvalue(float reward, long currentStateNumber) {
    transitions.addTransition(prevState, prevAction, currentStateNumber);
    
    // Update Q values using the following update rule:
    //
    // Q(prevState, prevAction) =
    //     (1 - alpha) * Qprev + alpha * (reward + gamma * maxQ)
    //
    // where alpha = learningRate / # prevState/prevAction visited.
    
    boolean shouldPrint = prevState == 1026 && prevAction == 2;
    if (shouldPrint) {
      Logger.println(3, "++++++++++++++++++++++");
      Logger.println(3, 
          "current: " + currentStateNumber + " reward: " + reward);
    }
    
    int bestAction = getBestAction(currentStateNumber);
    float maxQ = getActionsQValues(currentStateNumber)[bestAction];

    float[] prevQs = getActionsQValues(prevState);
    float prevQ = prevQs[prevAction];
    
    float alpha =
        LearningParams.BASE_LEARNING_RATE +
        learningRate / transitions.getCount(prevState, prevAction);
    
    Logger.println(4, "count: " + prevState + ", " + prevAction + " = " +
        transitions.getCount(prevState, prevAction));
    
    float newQ = (1 - alpha) * prevQ +  alpha * (reward + gammaValue * maxQ);
    
    prevQs[prevAction] = newQ;
    
    if (shouldPrint) {
      Logger.println(3, "prevQ: " + prevQ + " maxQ: " + maxQ + " newQ: " + newQ);
    }
  }
  
  @Override
  float[] getInitialQvalues(long stateNumber) {
    float[] initialQvalues = new float[actionRange];
    for (int i = 0; i < actionRange; i++) {
      // Set as random float ranged (-.1, .1), check whether makes sense.
      initialQvalues[i] = (float) (randomGenerator.nextFloat() * 0.2 - 0.1);
    }
    return initialQvalues;
  }

  public void dumpQtable(String logfile) {
    Logger.println(1, "** Dumping Qtable to " + logfile + " **");
    
    StringBuilder sb = new StringBuilder();
    for (long key : getTable().keySet()){
      sb.append(printState(key));
      sb.append("\n");
    }

    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(logfile));
      writer.write(sb.toString());
      writer.close();
    } catch (IOException x) {
      System.err.println("Failed to write qtable to: " + logfile);
    }
  }
  
  public String printState(long key) {
    return String.format(
        "%d:%s:%s",
        key,
        Utils.join(getTable().get(key), " "),
        Utils.join(transitions.getCounts(key), " "));
  }
  
  private void parseState(String line) {
    String[] tokens = line.split(":");
    long state = Long.valueOf(tokens[0]);
    String[] qvalueStrings = tokens[1].split(" ");
    String[] countStrings = tokens[2].split(" ");
    float[] qvalues = getActionsQValues(state);
    for (int i = 0; i < actionRange; i++) {
      qvalues[i] = Float.valueOf(qvalueStrings[i]);
      transitions.setCount(state, i, Integer.valueOf(countStrings[i]));
    }
  }
  
  public void loadQtable(String logfile) {
    Logger.println(1, "** Loading Qtable from " + logfile + " **");
    try {
      BufferedReader reader = new BufferedReader(new FileReader(logfile));
      String line;
      while ((line = reader.readLine()) != null) {
        parseState(line);
      }
      reader.close();
    } catch (Exception e) {
      System.err.println("Failed to load qtable from: " + logfile);
    }
  }
}
