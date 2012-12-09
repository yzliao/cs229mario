package edu.stanford.cs229.agents;

public class LearningParams {
  
  public static boolean DEBUG = false;
  
  public static final boolean DUMP_QTABLE = true;
  
  public static final int NUM_TRAINING_ITERATIONS = 800;
  
  public static final int NUM_EVAL_ITERATIONS = 4;
  
  public static final float EVAL_LEARNING_RATE = 0f;
  public static final float EVAL_EXPLORATION_CHANCE = 0.0f;

  public static final int OBSERVATION_SIZE = 5;
  
  public static final int MIN_MOVE_DISTANCE = 2;
  public static final int MAX_NUM_STUCK_FRAMES = 25;

  // E-GREEDY Q-LEARNING SPECIFIC VARIABLES
  /**
   * for e-greedy Q-learning, when taking an action a random number is checked
   * against the explorationChance variable: if the number is below the
   * explorationChance, then exploration takes place picking an action at
   * random. Note that the explorationChance is not a final because it is
   * customary that the exploration chance changes as the training goes on.
   */
  public static final float EXPLORATION_CHANCE = 0.3f;

  /**
   * the discount factor is saved as the gammaValue variable. The discount
   * factor determines the importance of future rewards. If the gammaValue is 0
   * then the AI will only consider immediate rewards, while with a gammaValue
   * near 1 (but below 1) the AI will try to maximize the long-term reward even
   * if it is many moves away.
   */
  public static final float GAMMA_VALUE = 0.2f;

  /**
   * the learningRate determines how new information affects accumulated
   * information from previous instances. If the learningRate is 1, then the new
   * information completely overrides any previous information. Note that the
   * learningRate is not a final because it is customary that the exploration
   * chance changes as the training goes on.
   */
  public static final float LEARNING_RATE = 0.9999f;
  
  public static final float BASE_LEARNING_RATE = 1 - LEARNING_RATE;
  
  public static final class REWARD_PARAMS {
    public static final int distance = 6;
    public static final int elevation = 8;
    public static final int collision = 0;
    public static final int win = 0;
    public static final int princes = 0;
    public static final int killedByFire = 0;
    public static final int killedByStomp = 0;
    public static final int stuck = -10;
    
    public static final int mode = 0;
    public static final int coins = 0;
    public static final int flowerFire = 0;
    public static final int kills = 0;
    public static final int killedByShell = 0;
    public static final int mushroom = 0;
    public static final int timeLeft = 0;
    public static final int hiddenBlock = 0;
    public static final int greenMushroom = 0;
    public static final int stomp = 0;
  };
  
  public static final int TIME_PENALTY = 0;
}
