package edu.stanford.cs229.agents;

import java.util.Hashtable;

public class TransitionTable {
  
  private static final float DEFAULT_PROBABILITY = 0.000001F;
  
  public static class ActionData {
    private int actionCount = 0;
    
    private Hashtable<Integer, Integer> transitions =
        new Hashtable<Integer, Integer>();
    
    public float getProbability(int toState) {
      if (!transitions.containsKey(toState)) {
        return DEFAULT_PROBABILITY;
      }
      return (1.0F * transitions.get(toState)) / actionCount;
    }
    
    public int getActionCount() {
      return actionCount;
    }
    
    public void addTransition(int toState) {
      actionCount += 1;
      
      if (!transitions.containsKey(toState)) {
        transitions.put(toState, 0);
      }
      transitions.put(toState, transitions.get(toState) + 1);
    }
  }

  Hashtable<Integer, Hashtable<Integer, ActionData>> stateCounter =
      new Hashtable<Integer, Hashtable<Integer, ActionData>>();

  private Hashtable<Integer, ActionData> getState(int state) {
    if (!stateCounter.containsKey(state)) {
      stateCounter.put(state, new Hashtable<Integer, ActionData>());
    }
    return stateCounter.get(state);
  }
  
  private ActionData getActionData(int fromState, int action) {
    ActionData actionData = getState(fromState).get(action);
    if (actionData == null) {
      actionData = new ActionData();
      getState(fromState).put(action, actionData);
    }
    return actionData;
  }
  
  public void addTransition(int fromState, int action, int toState) {
    getActionData(fromState, action).addTransition(toState);
  }
  
  public int getCount(int fromState, int action) {
    return getActionData(fromState, action).getActionCount();
  }
}
