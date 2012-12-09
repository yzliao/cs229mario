package edu.stanford.cs229.agents;

import ch.idsia.benchmark.mario.engine.sprites.Mario;

public enum MarioAction {

  DO_NOTHING(0),
  
  LEFT(1, Mario.KEY_LEFT),
  RIGHT(2, Mario.KEY_RIGHT),
  JUMP(3, Mario.KEY_JUMP),
  FIRE(4, Mario.KEY_SPEED),
  
  LEFT_JUMP(5, Mario.KEY_LEFT, Mario.KEY_JUMP),
  RIGHT_JUMP(6, Mario.KEY_RIGHT, Mario.KEY_JUMP),
  
  LEFT_FIRE(7, Mario.KEY_LEFT, Mario.KEY_SPEED),
  RIGHT_FIRE(8, Mario.KEY_RIGHT, Mario.KEY_SPEED),
  
  JUMP_FIRE(9, Mario.KEY_JUMP, Mario.KEY_SPEED),
  
  LEFT_JUMP_FIRE(10, Mario.KEY_LEFT, Mario.KEY_JUMP, Mario.KEY_SPEED),
  RIGHT_JUMP_FIRE(11, Mario.KEY_RIGHT, Mario.KEY_JUMP, Mario.KEY_SPEED);
  
  // Update the total number when adding new actions.
  public static final int TOTAL_ACTIONS = 12;
  
  private final int actionNumber;
  private final boolean[] action;
  
  private MarioAction(int actionNumber, int... keys) {
    this.actionNumber = actionNumber;
    
    this.action = new boolean[6];
    for (int key : keys) {
      this.action[key] = true;
    }
  }
  
  public int getActionNumber() {
    return actionNumber;
  }
  
  public boolean[] getAction() {
    return action;
  }
  
  public static boolean[] getAction(int actionNumber) {
    return MarioAction.values()[actionNumber].getAction();
  }
}
