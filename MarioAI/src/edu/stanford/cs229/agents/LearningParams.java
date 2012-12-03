package edu.stanford.cs229.agents;

import ch.idsia.benchmark.tasks.SystemOfValues;

public class LearningParams {
  
  public static final boolean DEBUG = false;
  
  public static final int NUM_TRAINING_ITERATIONS = 50;

  public static final int OBSERVATION_SIZE = 5;

  // E-GREEDY Q-LEARNING SPECIFIC VARIABLES
  /**
   * for e-greedy Q-learning, when taking an action a random number is checked
   * against the explorationChance variable: if the number is below the
   * explorationChance, then exploration takes place picking an action at
   * random. Note that the explorationChance is not a final because it is
   * customary that the exploration chance changes as the training goes on.
   */
  public static final float EXPLORATION_CHANCE = 0.1f;

  /**
   * the discount factor is saved as the gammaValue variable. The discount
   * factor determines the importance of future rewards. If the gammaValue is 0
   * then the AI will only consider immediate rewards, while with a gammaValue
   * near 1 (but below 1) the AI will try to maximize the long-term reward even
   * if it is many moves away.
   */
  public static final float GAMA_VALUE = 0.9f;

  /**
   * the learningRate determines how new information affects accumulated
   * information from previous instances. If the learningRate is 1, then the new
   * information completely overrides any previous information. Note that the
   * learningRate is not a final because it is customary that the exploration
   * chance changes as the training goes on.
   */
  public static final float LEARNING_RATE = 0.15f;
  
  public static final SystemOfValues REWARD_PARAMS = new SystemOfValues();

  static {
    REWARD_PARAMS.distance = 10;
    REWARD_PARAMS.win = 0;
    REWARD_PARAMS.mode = 30;
    REWARD_PARAMS.coins = 0;
    REWARD_PARAMS.flowerFire = 0;
    REWARD_PARAMS.kills = 20;
    REWARD_PARAMS.killedByFire = 0;
    REWARD_PARAMS.killedByShell = 0;
    REWARD_PARAMS.killedByStomp = 0;
    REWARD_PARAMS.mushroom = 0;
    REWARD_PARAMS.timeLeft = 0;
    REWARD_PARAMS.hiddenBlock = 0;
    REWARD_PARAMS.greenMushroom = 0;
    REWARD_PARAMS.stomp = 0;
  };
  
  public static final int TIME_PENALTY = 2;
}
