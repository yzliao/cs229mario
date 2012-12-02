package edu.stanford.cs229.agents;

import java.util.Hashtable;

public class TransitionTable {

  public static class StatePair {
    private final int fromState;
    private final int toState;

    private StatePair(int from, int to) {
      this.fromState = from;
      this.toState = to;
    }
    
    public int getFromState() {
      return fromState;
    }
    
    public int getToState() {
      return toState;
    }
    
    public static StatePair of (int from, int to) {
      return new StatePair(from, to);
    }
  }
  
  public static class ActionData {
    private int actionCount = 0;
    private Hashtable<Integer, Integer> transitions =
        new Hashtable<Integer, Integer>();
    
    public float getProbability(int toState) {
      if (!transitions.containsKey(toState)) {
        return 0;
      }
      return (1.0F * transitions.get(toState)) / actionCount;
    }
  }

  Hashtable<Integer, Hashtable<Integer, ActionData>> stateCounter =
      new Hashtable<Integer, Hashtable<Integer, ActionData>>();

  private static final float DEFAULT_PROBABILITY = 0.00003F;
  
  private float getTransitionProbability(
      int fromState, int action, int toState) {
    if (!stateCounter.containsKey(fromState)) {
      return DEFAULT_PROBABILITY;
    }
    Hashtable<Integer, ActionData> actionTable = stateCounter.get(fromState);
    if (!actionTable.containsKey(action)) {
      return DEFAULT_PROBABILITY;
    }
    return actionTable.get(action).getProbability(toState);
  }
  
  private Hashtable<Integer, ActionData> getState(int state) {
    if (!stateCounter.containsKey(state)) {
      stateCounter.put(state, new Hashtable<Integer, ActionData>());
    }
    return stateCounter.get(state);
  }
  
  public ActionData getActionData(int fromState, int action) {
    ActionData actionData = getState(fromState).get(action);
    if (actionData == null) {
      actionData = new ActionData();
      getState(fromState).put(action, actionData);
    }
    return actionData;
  }
  
  public void rememberTransition(int fromState, int action, int toState) {
    
    Hashtable<Integer, ActionData> actionTable = stateCounter.get(fromState);
    // TODO
  }
}
